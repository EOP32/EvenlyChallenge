package challenge.mapapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = false)
    val fsq_id: String,
    val name: String,
    val lat: Double?,
    val lng: Double?,
    val category: String,
    val address: String
)
