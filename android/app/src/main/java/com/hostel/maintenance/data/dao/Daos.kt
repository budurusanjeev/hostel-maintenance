package com.hostel.maintenance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hostel.maintenance.data.entity.MaintenanceEntity
import com.hostel.maintenance.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY name ASC")
    fun observeAll(): Flow<List<StudentEntity>>

    @Insert
    suspend fun insert(student: StudentEntity): Long

    @Update
    suspend fun update(student: StudentEntity)

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): StudentEntity?

    @Query("SELECT * FROM students")
    suspend fun getAll(): List<StudentEntity>

    @Query("SELECT COUNT(*) FROM students")
    suspend fun count(): Int

    @Delete
    suspend fun delete(student: StudentEntity)
}

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_requests ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<MaintenanceEntity>>

    @Insert
    suspend fun insert(request: MaintenanceEntity): Long

    @Update
    suspend fun update(request: MaintenanceEntity)

    @Query("SELECT * FROM maintenance_requests WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MaintenanceEntity?
}
