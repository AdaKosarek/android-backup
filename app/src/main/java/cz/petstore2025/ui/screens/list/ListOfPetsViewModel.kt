package cz.petstore2025.ui.screens.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<ListOfPetsScreenUIState> = MutableStateFlow(value = ListOfPetsScreenUIState())
    val uiState: StateFlow<ListOfPetsScreenUIState> get() = _uiState


}