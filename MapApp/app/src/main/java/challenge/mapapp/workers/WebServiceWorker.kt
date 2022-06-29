package challenge.mapapp.workers

import android.app.Notification
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import challenge.mapapp.MapApplication
import challenge.mapapp.R
import challenge.mapapp.data.repositories.IPlacesRepository
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WebServiceWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: IPlacesRepository
) : CoroutineWorker(context, params) {
    private val NOTIFICATION_ID = 1

    companion object {
        const val SUCCESS = "success"
        const val FAILURE= "failure"
        const val RESULT = "result"
    }

    override suspend fun doWork(): Result {
        return try {
            repository.loadPlacesData().also {
                repository.storeResults(it.convertToResultsList())
            }
            setForeground(getForegroundInfo())

            Result.success(workDataOf(RESULT to SUCCESS))
        } catch (e: Exception) {
            Result.failure(workDataOf(RESULT to FAILURE))
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    private fun createNotification() : Notification {
        return NotificationCompat.Builder(applicationContext, MapApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Coding Challenge") // should not be hardcoded
            .setContentText("MapApp loading data from server")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}