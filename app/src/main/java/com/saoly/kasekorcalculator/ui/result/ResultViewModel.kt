package com.saoly.kasekorcalculator.ui.result

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import com.saoly.kasekorcalculator.ui.common.formatInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: CropRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Populated by Navigation from the "result/{cropCycleId}" route argument
    private val cropCycleId: Int = savedStateHandle["cropCycleId"] ?: -1

    private val _cropCycle = MutableStateFlow<CropCycle?>(null)
    val cropCycle: StateFlow<CropCycle?> = _cropCycle.asStateFlow()

    // Price per the cycle's yield unit, as typed
    var testBuyerPrice by mutableStateOf("")

    init {
        viewModelScope.launch {
            repository.getCropCycleById(cropCycleId).collect { cycle ->
                _cropCycle.value = cycle
                if (cycle != null && cycle.buyerPrice > 0.0 && testBuyerPrice.isEmpty()) {
                    testBuyerPrice = formatInput(cycle.yieldUnit.pricePerUnit(cycle.buyerPrice))
                }
            }
        }
    }

    // Only updates the on-screen simulation; nothing is persisted until saveBuyerPrice()
    fun updateBuyerPrice(priceStr: String) {
        testBuyerPrice = priceStr
    }

    fun saveBuyerPrice(onSaved: () -> Unit = {}) {
        val pricePerUnit = testBuyerPrice.replace(",", "").toDoubleOrNull()?.takeIf { it >= 0.0 } ?: 0.0
        val current = _cropCycle.value
        viewModelScope.launch {
            val price = current?.yieldUnit?.pricePerKg(pricePerUnit) ?: 0.0
            if (current != null && current.buyerPrice != price) {
                repository.insertCropCycle(current.copy(buyerPrice = price))
            }
            onSaved()
        }
    }
}
