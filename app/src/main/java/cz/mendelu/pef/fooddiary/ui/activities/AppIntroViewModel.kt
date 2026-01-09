package cz.mendelu.pef.fooddiary.ui.activities
import androidx.lifecycle.ViewModel
import cz.mendelu.pef.fooddiary.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppIntroViewModel @Inject constructor(private val dataStoreRepository: IDataStoreRepository) : ViewModel() {
    suspend fun setFirstRun(){
        dataStoreRepository.setFirstRun()
    }
}
