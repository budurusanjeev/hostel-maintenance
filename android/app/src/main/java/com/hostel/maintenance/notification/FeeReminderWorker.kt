package com.hostel.maintenance.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hostel.maintenance.HostelApp
import java.time.LocalDate

class FeeReminderWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? HostelApp ?: return Result.retry()
        val studentId = inputData.getLong(KEY_STUDENT_ID, -1L)
        val students = if (studentId > 0) {
            listOfNotNull(app.repository.getStudent(studentId))
        } else {
            app.repository.getAllStudents()
        }
        val today = LocalDate.now()

        students.forEach { student ->
            if (FeeReminderScheduler.isReminderDue(student, today)) {
                FeeReminderNotifier.show(applicationContext, student)
            }
        }
        return Result.success()
    }

    companion object {
        const val KEY_STUDENT_ID = "studentId"
    }
}
