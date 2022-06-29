package challenge.mapapp.ui.viewModels

import androidx.lifecycle.ViewModel
import challenge.mapapp.data.local.Result
import challenge.mapapp.data.repositories.IPlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    val repository: IPlacesRepository
) : ViewModel() {

    suspend fun getResultsData(category: String): Flow<List<Result>> {
        return repository.getResultsForCategoryFlow(category)
    }
}