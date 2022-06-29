package challenge.mapapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import challenge.mapapp.data.dao.ResultsDao
import challenge.mapapp.data.local.entities.ResultEntity

@Database(entities = [ResultEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao
}