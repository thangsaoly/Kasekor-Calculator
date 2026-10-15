package com.saoly.kasekorcalculator.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CropRepository
) : ViewModel() {

    var name by mutableStateOf("")
    // Set by the screen to the first localized crop type label when blank
    var cropType by mutableStateOf("")
    var seedsCost by mutableStateOf("")
    var fertilizerCost by mutableStateOf("")
    var laborCost by mutableStateOf("")
    var fuelCost by mutableStateOf("")
    var expectedYield by mutableStateOf("")

    private val _saveResult = MutableSharedFlow<SaveEvent>()
    val saveResult: SharedFlow<SaveEvent> = _saveResult

    val totalExpensesLive: Double
        get() {
            return listOf(seedsCost, fertilizerCost, laborCost, fuelCost)
                .sumOf { parseCost(it) ?: 0.0 }
        }

    // Accepts thousands separators ("1,500,000"); returns null for invalid or negative input
    private fun parseAmount(input: String): Double? =
        input.replace(",", "").trim().toDoubleOrNull()?.takeIf { it >= 0.0 && it.isFinite() }

    // Blank cost fields count as zero
    private fun parseCost(input: String): Double? =
        if (input.isBlank()) 0.0 else parseAmount(input)

    fun saveCropCycle() {
        val yieldVal = parseAmount(expectedYield)
        if (name.isBlank() || yieldVal == null || yieldVal <= 0.0) {
            viewModelScope.launch {
                _saveResult.emit(SaveEvent.ValidationError("សូមបំពេញឈ្មោះវដ្តដំណាំ និងទិន្នផលឱ្យបានត្រឹមត្រូវ"))
            }
            return
        }

        val seeds = parseCost(seedsCost)
        val fertilizer = parseCost(fertilizerCost)
        val labor = parseCost(laborCost)
        val fuel = parseCost(fuelCost)
        if (seeds == null || fertilizer == null || labor == null || fuel == null) {
            viewModelScope.launch {
                _saveResult.emit(SaveEvent.ValidationError("សូមបញ្ចូលលេខឱ្យបានត្រឹមត្រូវ"))
            }
            return
        }

        viewModelScope.launch {
            try {
                val cropCycle = CropCycle(
                    name = name.trim(),
                    cropType = cropType,
                    seedsCost = seeds,
                    fertilizerCost = fertilizer,
                    laborCost = labor,
                    fuelCost = fuel,
                    expectedYield = yieldVal
                )
                val newId = repository.insertCropCycle(cropCycle)
                _saveResult.emit(SaveEvent.Success(newId))
            } catch (e: Exception) {
                _saveResult.emit(SaveEvent.Error(e.localizedMessage ?: "Error saving data"))
            }
        }
    }

    sealed interface SaveEvent {
        data class Success(val cropCycleId: Long) : SaveEvent
        data class ValidationError(val error: String) : SaveEvent
        data class Error(val message: String) : SaveEvent
    }
}
