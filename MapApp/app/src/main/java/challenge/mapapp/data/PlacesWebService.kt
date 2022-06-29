package challenge.mapapp.data

import challenge.mapapp.data.remote.PlacesDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesWebService {
    @GET("/v3/places/search")
    suspend fun getPlaces(@Query("ll") latLong: String,
                          @Query("limit") number: Int): Response<PlacesDataDto>
}