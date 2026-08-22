package com.hostel.maintenance

import android.app.Application
import com.hostel.maintenance.data.HostelDatabase
import com.hostel.maintenance.data.HostelRepository
import com.hostel.maintenance.notification.FeeReminderNotifier
import com.hostel.maintenance.notification.FeeReminderScheduler

class HostelApp : Application() {
    val database by lazy { HostelDatabase.getInstance(this) }
    val repository by lazy { HostelRepository(database) }

    override fun onCreate() {
        super.onCreate()
        FeeReminderNotifier.createChannel(this)
        FeeReminderScheduler.enqueuePeriodic(this)
    }
}
