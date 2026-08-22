package com.hostel.maintenance.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import com.hostel.maintenance.model.FeeReminder
import com.hostel.maintenance.model.FeeReminderStatus
import com.hostel.maintenance.notification.FeeReminderScheduler
import com.hostel.maintenance.viewmodel.HostelViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(
    viewModel: HostelViewModel,
    requests: List<MaintenanceEntity>,
    modifier: Modifier = Modifier,
) {
    var showForm by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<MaintenanceEntity?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val zone = ZoneId.systemDefault()
    val currentMonth = YearMonth.now()
    val monthLabel = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val monthTotal = requests
        .filter { YearMonth.from(Instant.ofEpochMilli(it.createdAt).atZone(zone).toLocalDate()) == currentMonth }
        .sumOf { it.expense }
    val dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

    fun hideForm() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showForm = false
            editingItem = null
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add expense")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text("Maintenance", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Track hostel expenses", color = Color.Gray)
                Spacer(Modifier.height(8.dp))
            }

            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(monthLabel, color = Color.Gray)
                        Text(
                            "Total this month: ₹$monthTotal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            if (requests.isEmpty()) {
                item { Text("No maintenance expenses yet. Tap + to add one.") }
            } else {
                items(requests, key = { it.id }) { request ->
                    val date = Instant.ofEpochMilli(request.createdAt).atZone(zone).toLocalDate()
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(request.description, fontWeight = FontWeight.Bold)
                            Text("₹${request.expense}", style = MaterialTheme.typography.titleMedium)
                            Text(date.format(dateFormat), color = Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { editingItem = request },
                                modifier = Modifier.fillMaxWidth(),
                            ) { Text("Edit") }
                        }
                    }
                }
            }
        }
    }

    if (showForm || editingItem != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showForm = false
                editingItem = null
            },
            sheetState = sheetState,
        ) {
            MaintenanceFormSheet(
                title = if (editingItem != null) "Edit expense" else "New expense",
                initial = editingItem,
                onSave = { description, expense ->
                    val current = editingItem
                    if (current != null) {
                        viewModel.updateMaintenance(current, description, expense)
                    } else {
                        viewModel.addMaintenance(description, expense)
                    }
                    hideForm()
                },
            )
        }
    }
}

@Composable
private fun MaintenanceFormSheet(
    title: String,
    initial: MaintenanceEntity?,
    onSave: (description: String, expense: Int) -> Unit,
) {
    var description by remember(initial?.id) { mutableStateOf(initial?.description.orEmpty()) }
    var expense by remember(initial?.id) { mutableStateOf(initial?.expense?.toString().orEmpty()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            description,
            { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            expense,
            { expense = it },
            label = { Text("Expense (₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                val amount = expense.toIntOrNull() ?: return@Button
                if (description.isNotBlank() && amount >= 0) {
                    onSave(description, amount)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Save expense") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    viewModel: HostelViewModel,
    students: List<StudentEntity>,
    modifier: Modifier = Modifier,
) {
    var showAddStudent by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var editingStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var studentToDelete by remember { mutableStateOf<StudentEntity?>(null) }
    val formSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val detailsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun hideForm() {
        scope.launch { formSheetState.hide() }.invokeOnCompletion {
            showAddStudent = false
            editingStudent = null
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddStudent = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add student")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text("Students", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Tap a student to view details", color = Color.Gray)
                Spacer(Modifier.height(8.dp))
            }

            if (students.isEmpty()) {
                item { Text("No students registered yet. Tap + to add one.") }
            } else {
                items(students, key = { it.id }) { student ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable { selectedStudent = student },
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(student.name, fontWeight = FontWeight.Bold)
                            Text("Room ${student.room} · ${student.course} · ${student.year}")
                            Text("Fee: ₹${student.feeAmount} · Due: ${student.feeDueDate}", color = Color.Gray)
                            Text("Joined ${student.joinDate} · reminder ${FeeReminderScheduler.reminderDate(student.joinDate)}", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    val detailsStudent = selectedStudent?.let { selected ->
        students.find { it.id == selected.id } ?: selected
    }
    if (detailsStudent != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedStudent = null },
            sheetState = detailsSheetState,
        ) {
            StudentDetailsSheet(
                student = detailsStudent,
                onEdit = {
                    val current = detailsStudent
                    scope.launch { detailsSheetState.hide() }.invokeOnCompletion {
                        selectedStudent = null
                        editingStudent = current
                    }
                },
                onDelete = { studentToDelete = detailsStudent },
            )
        }
    }

    if (showAddStudent || editingStudent != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddStudent = false
                editingStudent = null
            },
            sheetState = formSheetState,
        ) {
            StudentFormSheet(
                title = if (editingStudent != null) "Edit student" else "New student",
                initial = editingStudent,
                onSave = { name, room, phone, email, course, year, parentName, parentPhone, amount, joinDate, feeDueDate ->
                    val current = editingStudent
                    if (current != null) {
                        viewModel.updateStudent(
                            current, name, room, phone, email, course, year,
                            parentName, parentPhone, amount, joinDate, feeDueDate,
                        )
                    } else {
                        viewModel.addStudent(
                            name, room, phone, email, course, year,
                            parentName, parentPhone, amount, joinDate, feeDueDate,
                        )
                    }
                    hideForm()
                },
            )
        }
    }

    studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = { Text("Delete student") },
            text = { Text("Remove ${student.name} from the hostel register? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteStudent(student.id)
                        studentToDelete = null
                        selectedStudent = null
                    },
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun StudentDetailsSheet(
    student: StudentEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(student.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        DetailRow("Room", student.room)
        DetailRow("Phone", student.phone)
        DetailRow("Email", student.email.ifBlank { "—" })
        DetailRow("Course", student.course)
        DetailRow("Year", student.year)
        DetailRow("Parent", student.parentName)
        DetailRow("Parent phone", student.parentPhone)
        DetailRow("Join date", student.joinDate)
        DetailRow("Fee reminder", FeeReminderScheduler.reminderDate(student.joinDate).toString())
        DetailRow("Monthly fee", "₹${student.feeAmount}")
        DetailRow("Fee due date", student.feeDueDate)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) { Text("Edit") }
        OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete") }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(label, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun StudentFormSheet(
    title: String,
    initial: StudentEntity?,
    onSave: (
        name: String,
        room: String,
        phone: String,
        email: String,
        course: String,
        year: String,
        parentName: String,
        parentPhone: String,
        amount: Int,
        joinDate: String,
        feeDueDate: String,
    ) -> Unit,
) {
    var name by remember(initial?.id) { mutableStateOf(initial?.name.orEmpty()) }
    var room by remember(initial?.id) { mutableStateOf(initial?.room.orEmpty()) }
    var phone by remember(initial?.id) { mutableStateOf(initial?.phone.orEmpty()) }
    var email by remember(initial?.id) { mutableStateOf(initial?.email.orEmpty()) }
    var course by remember(initial?.id) { mutableStateOf(initial?.course.orEmpty()) }
    var year by remember(initial?.id) { mutableStateOf(initial?.year ?: "1st Year") }
    var parentName by remember(initial?.id) { mutableStateOf(initial?.parentName.orEmpty()) }
    var parentPhone by remember(initial?.id) { mutableStateOf(initial?.parentPhone.orEmpty()) }
    var feeAmount by remember(initial?.id) { mutableStateOf(initial?.feeAmount?.toString() ?: "8000") }
    var joinDate by remember(initial?.id) {
        mutableStateOf(initial?.joinDate ?: LocalDate.now().toString())
    }
    var feeDueDate by remember(initial?.id) {
        mutableStateOf(initial?.feeDueDate ?: FeeReminderScheduler.reminderDate(LocalDate.now().toString()).toString())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(name, { name = it }, label = { Text("Student name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(room, { room = it }, label = { Text("Room") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(phone, { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(course, { course = it }, label = { Text("Course") }, modifier = Modifier.fillMaxWidth())
        YearDropdown(year) { year = it }
        OutlinedTextField(parentName, { parentName = it }, label = { Text("Parent name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(parentPhone, { parentPhone = it }, label = { Text("Parent phone") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            feeAmount,
            { feeAmount = it },
            label = { Text("Monthly fee (₹)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            joinDate,
            {
                joinDate = it
                runCatching { FeeReminderScheduler.reminderDate(it) }.getOrNull()?.let { due ->
                    feeDueDate = due.toString()
                }
            },
            label = { Text("Join date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(feeDueDate, { feeDueDate = it }, label = { Text("Fee due date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Text("A fee remainder notification is sent on the 5th of the next month after joining.", color = Color.Gray)
        Button(
            onClick = {
                val amount = feeAmount.toIntOrNull() ?: return@Button
                if (name.isBlank() || room.isBlank() || phone.isBlank() || course.isBlank() ||
                    parentName.isBlank() || parentPhone.isBlank() || joinDate.isBlank() || feeDueDate.isBlank()
                ) return@Button
                onSave(name, room, phone, email, course, year, parentName, parentPhone, amount, joinDate, feeDueDate)
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Save student details") }
    }
}

@Composable
fun FeeRemindersScreen(
    viewModel: HostelViewModel,
    reminders: List<FeeReminder>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Fee Reminders", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Fee remainder notifications are sent on the 5th of the month after each student joins.", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
        }

        if (reminders.isEmpty()) {
            item { Text("No upcoming or overdue fee reminders.") }
        } else {
            items(reminders, key = { it.student.id }) { reminder ->
                FeeReminderCard(reminder) { viewModel.markFeePaid(reminder.student.id) }
            }
        }
    }
}

@Composable
private fun FeeReminderCard(reminder: FeeReminder, onMarkPaid: () -> Unit) {
    val color = when (reminder.status) {
        FeeReminderStatus.OVERDUE -> Color(0xFFB42318)
        FeeReminderStatus.DUE_SOON -> Color(0xFFB54708)
        FeeReminderStatus.UPCOMING -> Color(0xFF2563EB)
        FeeReminderStatus.PAID -> Color(0xFF027A48)
    }
    val label = when (reminder.status) {
        FeeReminderStatus.OVERDUE -> "Overdue"
        FeeReminderStatus.DUE_SOON -> "Due Soon"
        FeeReminderStatus.UPCOMING -> "Upcoming"
        FeeReminderStatus.PAID -> "Paid"
    }
    val dueText = when {
        reminder.status == FeeReminderStatus.OVERDUE ->
            "${kotlin.math.abs(reminder.daysUntilDue)} day(s) overdue"
        reminder.daysUntilDue == 0 -> "Due today"
        else -> "Due in ${reminder.daysUntilDue} day(s)"
    }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(reminder.student.name, fontWeight = FontWeight.Bold)
                Text(label, color = color, fontWeight = FontWeight.SemiBold)
            }
            Text("Room ${reminder.student.room} · ₹${reminder.student.feeAmount} · Due ${reminder.student.feeDueDate}")
            Text(dueText, color = color)
            Text("Parent: ${reminder.student.parentName} (${reminder.student.parentPhone})")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onMarkPaid) { Text("Mark fee paid") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearDropdown(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Year") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { year ->
                DropdownMenuItem(text = { Text(year) }, onClick = { onSelected(year); expanded = false })
            }
        }
    }
}
