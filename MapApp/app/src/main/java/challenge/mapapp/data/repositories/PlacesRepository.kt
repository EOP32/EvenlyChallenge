package challenge.mapapp.data.repositories

import androidx.lifecycle.LiveData
import challenge.mapapp.data.PlacesWebService
import challenge.mapapp.data.dao.ResultsDao
import challenge.mapapp.data.local.Result
import challenge.mapapp.data.local.entities.ResultEntity
import challenge.mapapp.data.remote.PlacesDataDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val webService: PlacesWebService,
    private val resultsDao: ResultsDao
): IPlacesRepository {

    // should be stored into data store or shared preferences
    private val evenlyLatLng = "52.500342,13.425170"
    private val limit = 50

    override suspend fun loadPlacesData(): PlacesDataDto {
        val response = webService.getPlaces(evenlyLatLng, limit)

        if (!response.isSuccessful) throw Exception()
        return response.body() ?: throw Exception()
    }

    override suspend fun storeResults(results: List<Result>) {
        resultsDao.storeResultsData(results.map {
            ResultEntity(it.fsq_id, it.name, it.lat, it.lng, it.category, it.address)
        })
    }

    override suspend fun getResultsFlow(): Flow<List<Result>> {
        return resultsDao.getAllResultsFlow()
    }

    override suspend fun getCategories(): LiveData<List<String>> {
        return resultsDao.getCategories()
    }

    override suspend fun getResults(): List<Result> {
        return resultsDao.getAllResults()
    }

    override suspend fun getResultsForCategory(category: String): List<Result> {
        return resultsDao.getCategoriesAndResults(category)
    }

    override suspend fun getResultsForCategoryFlow(category: String): Flow<List<Result>> {
        return resultsDao.getCategoriesAndResultsFlow(category)
    }
}