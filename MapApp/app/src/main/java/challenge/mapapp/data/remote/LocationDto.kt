package challenge.mapapp.data.remote

data class LocationDto(
    val address: String,
    val country: String,
    val formatted_address: String,
    val postcode: String,
    val region: String
)