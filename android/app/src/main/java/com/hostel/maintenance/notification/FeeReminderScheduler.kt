package com.hostel.maintenance.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.hostel.maintenance.data.entity.StudentEntity
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object FeeReminderScheduler {
    const val ACTION_FEE_REMINDER = "com.hostel.maintenance.action.FEE_REMINDER"
    const val EXTRA_STUDENT_ID = "studentId"

    private const val PERIODIC_WORK = "fee_reminder_daily"

    fun reminderDate(joinDate: String): LocalDate {
        val joined = LocalDate.parse(joinDate)
        return YearMonth.from(joined).plusMonths(1).atDay(5)
    }

    fun isReminderDue(student: StudentEntity, today: LocalDate = LocalDate.now()): Boolean {
        val due = runCatching { LocalDate.parse(student.feeDueDate) }.getOrNull() ?: return false
        return today == due
    }

    fun enqueuePeriodic(context: Context) {
        val request = PeriodicWorkRequestBuilder<FeeReminderWorker>(12, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun scheduleStudent(context: Context, student: StudentEntity) {
        val trigger = reminderTrigger(student) ?: return
        val now = ZonedDateTime.now()
        if (!trigger.isAfter(now)) return

        scheduleAlarm(context, student.id, trigger.toInstant().toEpochMilli())

        val delay = Duration.between(now, trigger)
        val request = OneTimeWorkRequestBuilder<FeeReminderWorker>()
            .setInitialDelay(delay)
            .setInputData(workDataOf(FeeReminderWorker.KEY_STUDENT_ID to student.id))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            workName(student.id),
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    fun cancelStudent(context: Context, studentId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent(context, studentId))
        WorkManager.getInstance(context).cancelUniqueWork(workName(studentId))
    }

    private fun reminderTrigger(student: StudentEntity): ZonedDateTime? {
        val dueDay = runCatching { LocalDate.parse(student.feeDueDate) }.getOrNull()
            ?: runCatching { reminderDate(student.joinDate) }.getOrNull()
            ?: return null
        return dueDay.atTime(LocalTime.of(9, 0)).atZone(ZoneId.systemDefault())
    }

    private fun scheduleAlarm(context: Context, studentId: Long, triggerAtMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = alarmIntent(context, studentId)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms() -> {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
            }
            else -> {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pending)
            }
        }
    }

    private fun alarmIntent(context: Context, studentId: Long): PendingIntent {
        val intent = Intent(context, FeeReminderReceiver::class.java).apply {
            action = ACTION_FEE_REMINDER
            putExtra(EXTRA_STUDENT_ID, studentId)
        }
        return PendingIntent.getBroadcast(
            context,
            studentId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun workName(studentId: Long) = "fee_reminder_$studentId"
}
