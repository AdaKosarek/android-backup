package cz.petstore2025.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenActivityViewModel @Inject constructor(
    private val dataStoreRepository: IDataStoreRepository
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow<SplashScreenUiState>(SplashScreenUiState.Default)
    val splashScreenState: StateFlow<SplashScreenUiState> = _splashScreenState

    fun checkAppState(){
        viewModelScope.launch {
            if (!dataStoreRepository.getLoginSuccessful()){
                _splashScreenState.value = SplashScreenUiState.ShowLogin
            } else {
                _splashScreenState.value = SplashScreenUiState.ContinueToApp
            }

        }
    }
}