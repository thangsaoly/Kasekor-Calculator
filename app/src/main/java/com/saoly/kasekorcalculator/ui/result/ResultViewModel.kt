package com.saoly.kasekorcalculator.ui.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: CropRepository,
    private val cropCycleId: Int
) : ViewModel() {

    private val _cropCycle = MutableStateFlow<CropCycle?>(null)
    val cropCycle: StateFlow<CropCycle?> = _cropCycle.asStateFlow()

    var testBuyerPrice by mutableStateOf("")

    init {
        viewModelScope.launch {
            repository.getCropCycleById(cropCycleId).collect { cycle ->
                _cropCycle.value = cycle
                if (cycle != null && cycle.buyerPrice > 0.0 && testBuyerPrice.isEmpty()) {
                    testBuyerPrice = cycle.buyerPrice.toInt().toString()
                }
            }
        }
    }

    fun updateBuyerPrice(priceStr: String) {
        testBuyerPrice = priceStr
        val price = priceStr.toDoubleOrNull() ?: 0.0
        val current = _cropCycle.value
        if (current != null) {
            viewModelScope.launch {
                repository.insertCropCycle(current.copy(buyerPrice = price))
            }
        }
    }
}

class ResultViewModelFactory(
    private val repository: CropRepository,
    private val cropCycleId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository, cropCycleId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
