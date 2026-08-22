package com.hostel.maintenance

import android.app.Application
import com.hostel.maintenance.data.HostelDatabase
import com.hostel.maintenance.data.HostelRepository

class HostelApp : Application() {
    val database by lazy { HostelDatabase.getInstance(this) }
    val repository by lazy { HostelRepository(database) }
}
