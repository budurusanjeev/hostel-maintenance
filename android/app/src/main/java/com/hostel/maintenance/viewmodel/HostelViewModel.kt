package com.hostel.maintenance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hostel.maintenance.data.HostelRepository
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import com.hostel.maintenance.model.FeeReminder
import com.hostel.maintenance.notification.FeeReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HostelViewModel(
    application: Application,
    private val repository: HostelRepository,
) : AndroidViewModel(application) {
    val students: StateFlow<List<StudentEntity>> = repository.students
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val maintenanceRequests: StateFlow<List<MaintenanceEntity>> = repository.maintenanceRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val feeReminders: StateFlow<List<FeeReminder>> = repository.feeReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            seedIfNeeded()
            repository.getAllStudents().forEach { student ->
                FeeReminderScheduler.scheduleStudent(getApplication(), student)
            }
        }
    }

    private suspend fun seedIfNeeded() {
        if (repository.studentCount() > 0) return

        val today = LocalDate.now()
        val firstDue = FeeReminderScheduler.reminderDate(today.toString()).toString()
        addAndSchedule(
            StudentEntity(
                name = "Rahul Sharma",
                room = "A-101",
                phone = "9876543210",
                email = "rahul@example.com",
                course = "Computer Science",
                year = "2nd Year",
                parentName = "Anil Sharma",
                parentPhone = "9876501234",
                feeAmount = 8000,
                joinDate = today.toString(),
                feeDueDate = firstDue,
            ),
        )
        addAndSchedule(
            StudentEntity(
                name = "Priya Nair",
                room = "B-204",
                phone = "9123456780",
                email = "priya@example.com",
                course = "Electronics",
                year = "1st Year",
                parentName = "Lakshmi Nair",
                parentPhone = "9123409876",
                feeAmount = 7500,
                joinDate = today.toString(),
                feeDueDate = firstDue,
            ),
        )
        repository.addMaintenance(
            MaintenanceEntity(
                description = "Plumbing repair",
                expense = 1500,
            ),
        )
    }

    fun addStudent(
        name: String,
        room: String,
        phone: String,
        email: String,
        course: String,
        year: String,
        parentName: String,
        parentPhone: String,
        feeAmount: Int,
        joinDate: String,
        feeDueDate: String,
    ) {
        viewModelScope.launch {
            addAndSchedule(
                StudentEntity(
                    name = name.trim(),
                    room = room.trim(),
                    phone = phone.trim(),
                    email = email.trim(),
                    course = course.trim(),
                    year = year.trim(),
                    parentName = parentName.trim(),
                    parentPhone = parentPhone.trim(),
                    feeAmount = feeAmount,
                    joinDate = joinDate.trim(),
                    feeDueDate = feeDueDate.trim(),
                ),
            )
        }
    }

    fun updateStudent(
        student: StudentEntity,
        name: String,
        room: String,
        phone: String,
        email: String,
        course: String,
        year: String,
        parentName: String,
        parentPhone: String,
        feeAmount: Int,
        joinDate: String,
        feeDueDate: String,
    ) {
        viewModelScope.launch {
            val updated = student.copy(
                name = name.trim(),
                room = room.trim(),
                phone = phone.trim(),
                email = email.trim(),
                course = course.trim(),
                year = year.trim(),
                parentName = parentName.trim(),
                parentPhone = parentPhone.trim(),
                feeAmount = feeAmount,
                joinDate = joinDate.trim(),
                feeDueDate = feeDueDate.trim(),
            )
            repository.updateStudent(updated)
            FeeReminderScheduler.scheduleStudent(getApplication(), updated)
        }
    }

    fun deleteStudent(studentId: Long) {
        viewModelScope.launch {
            FeeReminderScheduler.cancelStudent(getApplication(), studentId)
            repository.deleteStudent(studentId)
        }
    }

    fun markFeePaid(studentId: Long) {
        viewModelScope.launch {
            repository.markFeePaid(studentId)
            val updated = repository.getStudent(studentId) ?: return@launch
            FeeReminderScheduler.scheduleStudent(getApplication(), updated)
        }
    }

    fun addMaintenance(description: String, expense: Int) {
        viewModelScope.launch {
            repository.addMaintenance(
                MaintenanceEntity(
                    description = description.trim(),
                    expense = expense,
                ),
            )
        }
    }

    fun updateMaintenance(item: MaintenanceEntity, description: String, expense: Int) {
        viewModelScope.launch {
            repository.updateMaintenance(
                item.copy(
                    description = description.trim(),
                    expense = expense,
                ),
            )
        }
    }

    private suspend fun addAndSchedule(student: StudentEntity) {
        val id = repository.addStudent(student)
        FeeReminderScheduler.scheduleStudent(getApplication(), student.copy(id = id))
    }

    class Factory(
        private val application: Application,
        private val repository: HostelRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HostelViewModel(application, repository) as T
        }
    }
}
