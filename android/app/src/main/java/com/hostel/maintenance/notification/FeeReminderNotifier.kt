package com.hostel.maintenance.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hostel.maintenance.MainActivity
import com.hostel.maintenance.R
import com.hostel.maintenance.data.entity.StudentEntity

object FeeReminderNotifier {
    const val CHANNEL_ID = "fee_reminders"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Fee reminders",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Fee remainder alerts on the 5th of the next month"
        }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun canNotify(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS,
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun show(context: Context, student: StudentEntity) {
        if (!canNotify(context)) return
        val reminderDay = student.feeDueDate
        val prefs = context.getSharedPreferences("fee_reminders", Context.MODE_PRIVATE)
        val key = "shown_${student.id}_$reminderDay"
        if (prefs.getBoolean(key, false)) return

        createChannel(context)

        val openApp = PendingIntent.getActivity(
            context,
            student.id.toInt(),
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setContentTitle("Fee remainder")
            .setContentText(
                "${student.name} (Room ${student.room}) — ₹${student.feeAmount} is due on the 5th of next month.",
            )
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "${student.name} joined on ${student.joinDate}. " +
                        "Fee remainder of ₹${student.feeAmount} is due on ${student.feeDueDate}.",
                ),
            )
            .setAutoCancel(true)
            .setContentIntent(openApp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(student.id.toInt(), notification)
        prefs.edit().putBoolean(key, true).apply()
    }
}
