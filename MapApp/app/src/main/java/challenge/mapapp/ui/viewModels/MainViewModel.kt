package challenge.mapapp.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.*
import challenge.mapapp.data.local.Result
import challenge.mapapp.data.repositories.PlacesRepository
import challenge.mapapp.workers.WebServiceWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: PlacesRepository
) : ViewModel() {
    private val workManager: WorkManager = WorkManager.getInstance(application)

    private val _categoriesAndResults = MutableStateFlow<List<CategoriesAndResults>>(emptyList())
    val categoriesAndResults: StateFlow<List<CategoriesAndResults>> = _categoriesAndResults

    // logic for categories and results mapping should added and be observed or loaded from database
    private val categories = mutableListOf("Restaurants", "Stores", "Coffee", "Cafés", "More")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getResultsFlow().collectLatest {
                assignResultsToCategories(categories)
            }
        }
    }

    private fun assignResultsToCategories(categories: List<String>) {
        val resultsForCategories = mutableListOf<CategoriesAndResults>()

        categories.forEach {
            viewModelScope.launch(Dispatchers.IO) {
                val results = repository.getResultsForCategory(it)
                resultsForCategories.add(CategoriesAndResults(it, results))
                _categoriesAndResults.emit(resultsForCategories)
            }
        }
    }

    fun startWork() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = repository.getResults()
            if (results.isEmpty()) {
                val initialLoad = OneTimeWorkRequestBuilder<WebServiceWorker>()
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .addTag("INITIAL_WORK") // should not be hardcoded
                    .build()

                workManager.beginWith(initialLoad).enqueue()
            } else {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .setRequiresCharging(true)
                    .build()

                val periodicWork = PeriodicWorkRequestBuilder<WebServiceWorker>(
                    1,
                    TimeUnit.DAYS,
                    1,
                    TimeUnit.HOURS
                )
                    .setConstraints(constraints)
                    .addTag("PERIODIC WORK")
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    "DAILY_DOWNLAOD",
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWork
                )
            }
        }
    }

    data class CategoriesAndResults(
        val category: String,
        val items: List<Result>
    )
}