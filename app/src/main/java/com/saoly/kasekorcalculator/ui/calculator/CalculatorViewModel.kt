package com.saoly.kasekorcalculator.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CalculatorViewModel(
    private val repository: CropRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var cropType by mutableStateOf("ស្រូវ/Rice")
    var seedsCost by mutableStateOf("")
    var fertilizerCost by mutableStateOf("")
    var laborCost by mutableStateOf("")
    var fuelCost by mutableStateOf("")
    var expectedYield by mutableStateOf("")

    private val _saveResult = MutableSharedFlow<SaveEvent>()
    val saveResult: SharedFlow<SaveEvent> = _saveResult

    val totalExpensesLive: Double
        get() {
            val seeds = seedsCost.toDoubleOrNull() ?: 0.0
            val fertilizer = fertilizerCost.toDoubleOrNull() ?: 0.0
            val labor = laborCost.toDoubleOrNull() ?: 0.0
            val fuel = fuelCost.toDoubleOrNull() ?: 0.0
            return seeds + fertilizer + labor + fuel
        }

    fun saveCropCycle() {
        val yieldVal = expectedYield.toDoubleOrNull()
        if (name.isBlank() || yieldVal == null || yieldVal <= 0.0) {
            viewModelScope.launch {
                _saveResult.emit(SaveEvent.ValidationError("សូមបំពេញឈ្មោះវដ្តដំណាំ និងទិន្នផលឱ្យបានត្រឹមត្រូវ"))
            }
            return
        }

        viewModelScope.launch {
            try {
                val seeds = seedsCost.toDoubleOrNull() ?: 0.0
                val fertilizer = fertilizerCost.toDoubleOrNull() ?: 0.0
                val labor = laborCost.toDoubleOrNull() ?: 0.0
                val fuel = fuelCost.toDoubleOrNull() ?: 0.0

                val cropCycle = CropCycle(
                    name = name,
                    cropType = cropType,
                    seedsCost = seeds,
                    fertilizerCost = fertilizer,
                    laborCost = labor,
                    fuelCost = fuel,
                    expectedYield = yieldVal
                )
                repository.insertCropCycle(cropCycle)
                
                // Fetch the newly inserted crop cycle (usually the latest since it's sorted by createdAt DESC)
                repository.getAllCropCycles().collect { list ->
                    val latest = list.firstOrNull { it.name == name && it.expectedYield == yieldVal }
                    if (latest != null && latest.id != null) {
                        _saveResult.emit(SaveEvent.Success(latest.id.toLong()))
                    } else {
                        _saveResult.emit(SaveEvent.Success(-1))
                    }
                }
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

class CalculatorViewModelFactory(private val repository: CropRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculatorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
