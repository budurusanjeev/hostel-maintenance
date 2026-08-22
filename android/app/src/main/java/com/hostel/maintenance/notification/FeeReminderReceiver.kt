package com.hostel.maintenance.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hostel.maintenance.HostelApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeeReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pending = goAsync()
        val appContext = context.applicationContext
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (intent.action) {
                    Intent.ACTION_BOOT_COMPLETED,
                    Intent.ACTION_LOCKED_BOOT_COMPLETED,
                    -> rescheduleAll(appContext)
                    FeeReminderScheduler.ACTION_FEE_REMINDER -> notifyStudent(appContext, intent)
                }
            } finally {
                pending.finish()
            }
        }
    }

    private suspend fun rescheduleAll(context: Context) {
        val app = context as? HostelApp ?: return
        app.repository.getAllStudents().forEach { student ->
            FeeReminderScheduler.scheduleStudent(context, student)
        }
    }

    private suspend fun notifyStudent(context: Context, intent: Intent) {
        val app = context as? HostelApp ?: return
        val studentId = intent.getLongExtra(FeeReminderScheduler.EXTRA_STUDENT_ID, -1L)
        val student = app.repository.getStudent(studentId) ?: return
        if (!FeeReminderScheduler.isReminderDue(student)) return
        FeeReminderNotifier.show(context, student)
    }
}
