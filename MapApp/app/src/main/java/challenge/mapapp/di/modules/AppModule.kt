package challenge.mapapp.di.modules

import android.content.Context
import androidx.room.Room
import challenge.mapapp.BuildConfig
import challenge.mapapp.data.PlacesWebService
import challenge.mapapp.data.dao.ResultsDao
import challenge.mapapp.data.local.database.AppDatabase
import challenge.mapapp.data.repositories.IPlacesRepository
import challenge.mapapp.data.repositories.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val AUTHORIZATION_HEADER = "Authorization"

    @Provides
    @Singleton
    fun providePlacesApi(): PlacesWebService {
        val okHttp = OkHttpClient().newBuilder().addInterceptor {
            val requestBuilder: Request.Builder = it.request().newBuilder()
            requestBuilder.header(AUTHORIZATION_HEADER, BuildConfig.PLACES_API_KEY)
            it.proceed(requestBuilder.build())
        }.build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(PlacesWebService::class.java)
    }

    @Provides
    @Singleton
    fun provideIPlacesRepository(webService: PlacesWebService, dao: ResultsDao): IPlacesRepository {
        return PlacesRepository(webService, dao)
    }

    @Provides
    @Singleton
    fun provideResultsDao(appDatabase: AppDatabase) = appDatabase.resultsDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}