package org.br.gillall.mesa1.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gillall.mesa.desafio1.database.getDatabase
import com.gillall.mesa.desafio1.repository.NewsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "org.br.gillall.mesa1.work.RefreshDataWorker"
        var token: String = ""
    }

    override suspend fun doWork(): Result {
        println("worker working")
        if (token.isNotEmpty()) {
            val db = getDatabase(applicationContext)
            val repository = NewsRepository.getInstance(db, "")
            try {
                repository.updateHighlights()
                repository.updateNews()
            } catch (e: HttpException) {
                return Result.retry()
            }
        } else {
            return Result.retry()
        }
        return Result.success()
    }
}