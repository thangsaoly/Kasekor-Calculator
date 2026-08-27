package com.saoly.kasekorcalculator.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CropRepository
) : ViewModel() {

    val cropCycles: StateFlow<List<CropCycle>> = repository.getAllCropCycles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCropCycle(cropCycle: CropCycle) {
        viewModelScope.launch {
            repository.deleteCropCycle(cropCycle)
        }
    }
}

class HomeViewModelFactory(private val repository: CropRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
