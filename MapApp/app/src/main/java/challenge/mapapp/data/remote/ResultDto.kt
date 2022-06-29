package challenge.mapapp.data.remote

data class ResultDto(
    val fsq_id: String,
    val geocodes: GeocodesDto?,
    val categories: List<CategoryDto>,
    val location: LocationDto,
    val name: String,
) {
    override fun toString(): String {
        return name
    }
}