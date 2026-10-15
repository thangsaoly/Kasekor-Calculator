package com.saoly.kasekorcalculator.data.repository

import com.saoly.kasekorcalculator.data.local.dao.CropCycleDao
import com.saoly.kasekorcalculator.data.local.entity.CropCycleEntity
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CropRepositoryImpl @Inject constructor(
    private val dao: CropCycleDao
) : CropRepository {

    override fun getAllCropCycles(): Flow<List<CropCycle>> {
        return dao.getAllCropCycles().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getCropCycleById(id: Int): Flow<CropCycle?> {
        return dao.getCropCycleById(id).map { it?.toDomainModel() }
    }

    override suspend fun insertCropCycle(cropCycle: CropCycle): Long {
        return dao.insertCropCycle(CropCycleEntity.fromDomainModel(cropCycle))
    }

    override suspend fun updateCropCycle(cropCycle: CropCycle) {
        dao.updateCropCycle(CropCycleEntity.fromDomainModel(cropCycle))
    }

    override suspend fun deleteCropCycle(cropCycle: CropCycle) {
        dao.deleteCropCycle(CropCycleEntity.fromDomainModel(cropCycle))
    }
}
