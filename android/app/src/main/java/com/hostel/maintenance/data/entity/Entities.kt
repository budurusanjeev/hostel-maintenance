package com.hostel.maintenance.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val room: String,
    val phone: String,
    val email: String,
    val course: String,
    val year: String,
    val parentName: String,
    val parentPhone: String,
    val feeAmount: Int,
    val feeDueDate: String,
    val joinDate: String,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(tableName = "maintenance_requests")
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val expense: Int,
    val createdAt: Long = System.currentTimeMillis(),
)
