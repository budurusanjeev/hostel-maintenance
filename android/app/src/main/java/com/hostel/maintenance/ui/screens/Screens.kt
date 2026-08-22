package com.hostel.maintenance.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import com.hostel.maintenance.model.FeeReminder
import com.hostel.maintenance.model.FeeReminderStatus
import com.hostel.maintenance.viewmodel.HostelViewModel
import java.time.LocalDate

@Composable
fun MaintenanceScreen(
    viewModel: HostelViewModel,
    requests: List<MaintenanceEntity>,
    modifier: Modifier = Modifier,
) {
    var room by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("medium") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Maintenance", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Log repair issues by room", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
        }

        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("New request", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(room, { room = it }, label = { Text("Room") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(issue, { issue = it }, label = { Text("Issue") }, modifier = Modifier.fillMaxWidth())
                    PriorityDropdown(priority) { priority = it }
                    Button(
                        onClick = {
                            if (room.isNotBlank() && issue.isNotBlank()) {
                                viewModel.addMaintenance(room, issue, priority)
                                room = ""
                                issue = ""
                                priority = "medium"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Submit request") }
                }
            }
        }

        item { Text("Open requests", fontWeight = FontWeight.SemiBold) }

        if (requests.isEmpty()) {
            item { Text("No maintenance requests yet.") }
        } else {
            items(requests, key = { it.id }) { request ->
                MaintenanceCard(request) { status ->
                    viewModel.updateMaintenanceStatus(request.id, status)
                }
            }
        }
    }
}

@Composable
private fun MaintenanceCard(request: MaintenanceEntity, onStatusChange: (String) -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(request.room, fontWeight = FontWeight.Bold)
            Text(request.issue)
            Text("Priority: ${request.priority} · Status: ${request.status}", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
            StatusDropdown(request.status, listOf("open", "in_progress", "resolved"), onStatusChange)
        }
    }
}

@Composable
fun StudentsScreen(
    viewModel: HostelViewModel,
    students: List<StudentEntity>,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("1st Year") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var feeAmount by remember { mutableStateOf("8000") }
    var feeDueDate by remember { mutableStateOf(LocalDate.now().plusDays(7).toString()) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Student Details", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Register hostel students", color = Color.Gray)
            Spacer(Modifier.height(8.dp))
        }

        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("New student entry", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(name, { name = it }, label = { Text("Student name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(room, { room = it }, label = { Text("Room") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(phone, { phone = it }, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(course, { course = it }, label = { Text("Course") }, modifier = Modifier.fillMaxWidth())
                    YearDropdown(year) { year = it }
                    OutlinedTextField(parentName, { parentName = it }, label = { Text("Parent name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(parentPhone, { parentPhone = it }, label = { Text("Parent phone") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(feeAmount, { feeAmount = it }, label = { Text("Monthly fee (₹)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(feeDueDate, { feeDueDate = it }, label = { Text("Fee due date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                    Button(
                        onClick = {
                            val amount = feeAmount.toIntOrNull() ?: return@Button
                            if (name.isBlank() || room.isBlank() || phone.isBlank() || course.isBlank() ||
                                parentName.isBlank() || parentPhone.isBlank() || feeDueDate.isBlank()
                            ) return@Button
                            viewModel.addStudent(name, room, phone, email, course, year, parentName, parentPhone, amount, feeDueDate)
                            name = ""; room = ""; phone = ""; email = ""; course = ""
                            parentName = ""; parentPhone = ""; feeAmount = "8000"
                            feeDueDate = LocalDate.now().plusDays(7).toString()
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Save student details") }
                }
            }
        }

        item { Text("Registered students", fontWeight = FontWeight.SemiBold) }

        if (students.isEmpty()) {
            item { Text("No students registered yet.") }
        } else {
            items(students, key = { it.id }) { student ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(student.name, fontWeight = FontWeight.Bold)
                        Text("Room ${student.room} · ${student.course} · ${student.year}")
                        Text("Phone: ${student.phone}")
                        Text("Parent: ${student.parentName} (${student.parentPhone})")
                        Text("Fee: ₹${student.feeAmount} · Due: ${student.feeDueDate}")
                    }
                }
            }
        }
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
            Text("Overdue and upcoming hostel fee payments", color = Color.Gray)
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
private fun PriorityDropdown(selected: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("low" to "Low", "medium" to "Medium", "high" to "High")
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
        OutlinedTextField(
            value = options.first { it.first == selected }.second,
            onValueChange = {},
            readOnly = true,
            label = { Text("Priority") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = { onSelected(value); expanded = false },
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdown(selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
        OutlinedTextField(
            value = selected.replace('_', ' '),
            onValueChange = {},
            readOnly = true,
            label = { Text("Update status") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.replace('_', ' ')) },
                    onClick = { onSelected(status); expanded = false },
                )
            }
        }
    }
}
