package challenge.mapapp.data.local

data class Result(
    val name: String,
    val fsq_id: String,
    val lat: Double?,
    val lng: Double?,
    val category: String,
    val address: String
)