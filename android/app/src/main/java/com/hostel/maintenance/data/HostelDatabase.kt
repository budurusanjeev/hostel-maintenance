package com.hostel.maintenance.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hostel.maintenance.data.dao.MaintenanceDao
import com.hostel.maintenance.data.dao.StudentDao
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity

@Database(
    entities = [StudentEntity::class, MaintenanceEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class HostelDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun maintenanceDao(): MaintenanceDao

    companion object {
        @Volatile
        private var instance: HostelDatabase? = null

        fun getInstance(context: Context): HostelDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HostelDatabase::class.java,
                    "hostel.db",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
