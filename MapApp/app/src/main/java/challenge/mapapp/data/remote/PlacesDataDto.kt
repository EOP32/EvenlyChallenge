package challenge.mapapp.data.remote

import challenge.mapapp.data.local.Result

data class PlacesDataDto(
    val results: List<ResultDto>
) {
    override fun toString(): String {
        return results.toString()
    }

    fun convertToResultsList(): List<Result> {
        return results.map {
            Result(
                it.name,
                it.fsq_id,
                it.geocodes?.main?.latitude,
                it.geocodes?.main?.longitude,
                getCategory(it.categories),
                it.location.formatted_address
            )
        }
    }

    private fun getCategory(categories: List<CategoryDto>): String {
        for (categoryItem: CategoryDto in categories) {
            // strings shouldn't be hard coded
            if (categoryItem.name.lowercase().contains("restaurant")) {
                return "Restaurants"
            }
            if (categoryItem.name.lowercase().contains("café")) {
                return "Cafés"
            }
            if (categoryItem.name.lowercase().contains("coffee")) {
                 return "Coffee"
            }
            if (categoryItem.name.lowercase().contains("store")) {
                return "Stores"
            }
        }
        return "More"
    }
}