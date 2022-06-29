package challenge.mapapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import challenge.mapapp.data.local.Result
import challenge.mapapp.data.local.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultsDao {
    @Insert(onConflict = REPLACE)
    fun storeResultsData(results: List<ResultEntity>)

    @Query("SELECT * FROM results")
    fun getAllResultsFlow(): Flow<List<Result>>

    @Query("SELECT * FROM results")
    fun getAllResults(): List<Result>

    @Query("SELECT DISTINCT category FROM results")
    fun getCategories(): LiveData<List<String>>

    @Query("SELECT * FROM results WHERE category=:category")
    fun getCategoriesAndResults(category: String): List<Result>

    @Query("SELECT * FROM results WHERE category=:category")
    fun getCategoriesAndResultsFlow(category: String): Flow<List<Result>>
}