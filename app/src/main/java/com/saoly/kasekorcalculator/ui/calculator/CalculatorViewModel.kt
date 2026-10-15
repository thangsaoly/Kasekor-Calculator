package com.saoly.kasekorcalculator.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.model.CropCycleForm
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import com.saoly.kasekorcalculator.ui.common.formatInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CropRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // -1 adds a new crop cycle; any other id edits that cycle
    private val cropCycleId: Int = savedStateHandle["cropCycleId"] ?: -1
    val isEditing: Boolean = cropCycleId != -1

    var name by mutableStateOf("")
    // Set by the screen to the first localized crop type label when blank
    var cropType by mutableStateOf("")
    var seedsCost by mutableStateOf("")
    var fertilizerCost by mutableStateOf("")
    var laborCost by mutableStateOf("")
    var fuelCost by mutableStateOf("")
    var expectedYield by mutableStateOf("") // in yieldUnit
    var currency by mutableStateOf(Currency.KHR)
    var yieldUnit by mutableStateOf(YieldUnit.KG)

    // The saved cycle being edited; null while loading or when adding
    private var original: CropCycle? = null
    private var initialFields: List<Any> = currentFields()

    // True once the user has changed anything since the form was opened or loaded
    val hasChanges: Boolean
        get() = currentFields() != initialFields

    private val _saveResult = MutableSharedFlow<SaveEvent>()
    val saveResult: SharedFlow<SaveEvent> = _saveResult

    init {
        if (isEditing) {
            // Load once so later database emissions never overwrite what the user is typing
            viewModelScope.launch {
                repository.getCropCycleById(cropCycleId).first()?.let { prefill(it) }
            }
        }
    }

    val totalExpensesLive: Double
        get() {
            return listOf(seedsCost, fertilizerCost, laborCost, fuelCost)
                .sumOf { parseCost(it) ?: 0.0 }
        }

    fun setDefaultCropType(label: String) {
        if (cropType.isBlank()) {
            cropType = label
            if (!isEditing) initialFields = currentFields()
        }
    }

    private fun prefill(cycle: CropCycle) {
        original = cycle
        name = cycle.name
        cropType = cycle.cropType
        seedsCost = formatInput(cycle.seedsCost)
        fertilizerCost = formatInput(cycle.fertilizerCost)
        laborCost = formatInput(cycle.laborCost)
        fuelCost = formatInput(cycle.fuelCost)
        expectedYield = formatInput(cycle.expectedYieldInUnit)
        currency = cycle.currency
        yieldUnit = cycle.yieldUnit
        initialFields = currentFields()
    }

    private fun currentFields(): List<Any> = listOf(
        name, cropType, seedsCost, fertilizerCost, laborCost, fuelCost, expectedYield, currency, yieldUnit
    )

    // Accepts thousands separators ("1,500,000"); returns null for invalid or negative input
    private fun parseAmount(input: String): Double? =
        input.replace(",", "").trim().toDoubleOrNull()?.takeIf { it >= 0.0 && it.isFinite() }

    // Blank cost fields count as zero
    private fun parseCost(input: String): Double? =
        if (input.isBlank()) 0.0 else parseAmount(input)

    fun saveCropCycle() {
        // Still loading the cycle being edited; saving now would create a duplicate
        if (isEditing && original == null) return

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

        val form = CropCycleForm(
            name = name,
            cropType = cropType,
            seedsCost = seeds,
            fertilizerCost = fertilizer,
            laborCost = labor,
            fuelCost = fuel,
            expectedYield = yieldVal,
            currency = currency,
            yieldUnit = yieldUnit
        )

        viewModelScope.launch {
            try {
                val current = original
                if (current != null) {
                    repository.updateCropCycle(form.toCropCycle(current))
                    _saveResult.emit(SaveEvent.Success(cropCycleId.toLong()))
                } else {
                    val newId = repository.insertCropCycle(form.toCropCycle())
                    _saveResult.emit(SaveEvent.Success(newId))
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
