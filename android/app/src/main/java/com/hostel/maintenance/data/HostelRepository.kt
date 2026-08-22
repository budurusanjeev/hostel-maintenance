package com.hostel.maintenance.data

import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import com.hostel.maintenance.model.FeeReminder
import com.hostel.maintenance.model.FeeReminderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HostelRepository(private val database: HostelDatabase) {
    private val studentDao = database.studentDao()
    private val maintenanceDao = database.maintenanceDao()

    val students: Flow<List<StudentEntity>> = studentDao.observeAll()
    val maintenanceRequests: Flow<List<MaintenanceEntity>> = maintenanceDao.observeAll()

    val feeReminders: Flow<List<FeeReminder>> = students.map { list ->
        list.mapNotNull { student ->
            val statusInfo = feeStatus(student.feeDueDate)
            if (statusInfo.status == FeeReminderStatus.PAID) {
                null
            } else {
                FeeReminder(student, statusInfo.status, statusInfo.daysUntilDue)
            }
        }.sortedBy { it.daysUntilDue }
    }

    suspend fun addStudent(student: StudentEntity): Long {
        return studentDao.insert(student)
    }

    suspend fun updateStudent(student: StudentEntity) {
        studentDao.update(student)
    }

    suspend fun deleteStudent(studentId: Long) {
        val student = studentDao.getById(studentId) ?: return
        studentDao.delete(student)
    }

    suspend fun markFeePaid(studentId: Long) {
        val student = studentDao.getById(studentId) ?: return
        val nextDue = LocalDate.parse(student.feeDueDate).plusMonths(1)
        studentDao.update(student.copy(feeDueDate = nextDue.toString()))
    }

    suspend fun addMaintenance(request: MaintenanceEntity) {
        maintenanceDao.insert(request)
    }

    suspend fun updateMaintenance(request: MaintenanceEntity) {
        maintenanceDao.update(request)
    }

    suspend fun studentCount(): Int = studentDao.count()

    suspend fun getStudent(id: Long): StudentEntity? = studentDao.getById(id)

    suspend fun getAllStudents(): List<StudentEntity> = studentDao.getAll()

    private fun feeStatus(dueDate: String, today: LocalDate = LocalDate.now()): FeeStatusInfo {
        val due = LocalDate.parse(dueDate)
        val days = ChronoUnit.DAYS.between(today, due).toInt()

        return when {
            days < 0 -> FeeStatusInfo(FeeReminderStatus.OVERDUE, days)
            days <= 7 -> FeeStatusInfo(FeeReminderStatus.DUE_SOON, days)
            days <= 30 -> FeeStatusInfo(FeeReminderStatus.UPCOMING, days)
            else -> FeeStatusInfo(FeeReminderStatus.PAID, days)
        }
    }

    private data class FeeStatusInfo(
        val status: FeeReminderStatus,
        val daysUntilDue: Int,
    )
}
