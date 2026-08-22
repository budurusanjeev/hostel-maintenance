package com.hostel.maintenance.model

import com.hostel.maintenance.data.entity.StudentEntity

enum class FeeReminderStatus {
    OVERDUE,
    DUE_SOON,
    UPCOMING,
    PAID,
}

data class FeeReminder(
    val student: StudentEntity,
    val status: FeeReminderStatus,
    val daysUntilDue: Int,
)
