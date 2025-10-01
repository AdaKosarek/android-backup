package cz.petstore2025.ui.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor(
    private val petsRemoteRepository: IPetsRemoteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListOfPetsScreenUIState> = MutableStateFlow(value = ListOfPetsScreenUIState())
    val uiState: StateFlow<ListOfPetsScreenUIState> get() = _uiState

    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                petsRemoteRepository.findByStatus("available")
            }

            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.i("Dotaz", "Connection error")
                }
                is CommunicationResult.Error -> {
                    Log.i("Dotaz", "Error" + result.error.code)
                }
                is CommunicationResult.Exception -> {
                    Log.i("Dotaz", "Exception" + result.exception.toString())
                }
                is CommunicationResult.Success<*> -> {
                    Log.i("Dotaz", "Success")
                }
            }




        }
    }

}