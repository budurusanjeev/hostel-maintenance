package com.hostel.maintenance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hostel.maintenance.data.HostelRepository
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import com.hostel.maintenance.model.FeeReminder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HostelViewModel(private val repository: HostelRepository) : ViewModel() {
    val students: StateFlow<List<StudentEntity>> = repository.students
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val maintenanceRequests: StateFlow<List<MaintenanceEntity>> = repository.maintenanceRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val feeReminders: StateFlow<List<FeeReminder>> = repository.feeReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            seedIfNeeded()
        }
    }

    private suspend fun seedIfNeeded() {
        if (repository.studentCount() > 0) return

        val today = LocalDate.now()
        repository.addStudent(
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
                feeDueDate = today.minusDays(2).toString(),
            ),
        )
        repository.addStudent(
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
                feeDueDate = today.plusDays(3).toString(),
            ),
        )
        repository.addMaintenance(
            MaintenanceEntity(
                room = "A-101",
                issue = "Leaking faucet in bathroom",
                priority = "medium",
                status = "open",
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
        feeDueDate: String,
    ) {
        viewModelScope.launch {
            repository.addStudent(
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
                    feeDueDate = feeDueDate,
                ),
            )
        }
    }

    fun markFeePaid(studentId: Long) {
        viewModelScope.launch {
            repository.markFeePaid(studentId)
        }
    }

    fun addMaintenance(room: String, issue: String, priority: String) {
        viewModelScope.launch {
            repository.addMaintenance(
                MaintenanceEntity(
                    room = room.trim(),
                    issue = issue.trim(),
                    priority = priority,
                    status = "open",
                ),
            )
        }
    }

    fun updateMaintenanceStatus(id: Long, status: String) {
        viewModelScope.launch {
            repository.updateMaintenanceStatus(id, status)
        }
    }

    class Factory(private val repository: HostelRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HostelViewModel(repository) as T
        }
    }
}
