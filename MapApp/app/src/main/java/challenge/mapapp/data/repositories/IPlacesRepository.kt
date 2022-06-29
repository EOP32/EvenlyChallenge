package challenge.mapapp.data.repositories

import androidx.lifecycle.LiveData
import challenge.mapapp.data.local.Result
import challenge.mapapp.data.remote.PlacesDataDto
import kotlinx.coroutines.flow.Flow

interface IPlacesRepository {
    suspend fun loadPlacesData(): PlacesDataDto
    suspend fun storeResults(results: List<Result>)
    suspend fun getResultsFlow(): Flow<List<Result>>
    suspend fun getCategories(): LiveData<List<String>>
    suspend fun getResults(): List<Result>
    suspend fun getResultsForCategory(category: String): List<Result>
    suspend fun getResultsForCategoryFlow(category: String): Flow<List<Result>>
}