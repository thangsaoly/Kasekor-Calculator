package com.saoly.kasekorcalculator.domain.repository

import com.saoly.kasekorcalculator.domain.model.CropCycle
import kotlinx.coroutines.flow.Flow

interface CropRepository {
    fun getAllCropCycles(): Flow<List<CropCycle>>
    fun getCropCycleById(id: Int): Flow<CropCycle?>
    suspend fun insertCropCycle(cropCycle: CropCycle): Long
    suspend fun deleteCropCycle(cropCycle: CropCycle)
}
