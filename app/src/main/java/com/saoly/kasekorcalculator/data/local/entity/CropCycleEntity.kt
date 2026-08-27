package com.saoly.kasekorcalculator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saoly.kasekorcalculator.domain.model.CropCycle

@Entity(tableName = "crop_cycles")
data class CropCycleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val cropType: String,
    val seedsCost: Double,
    val fertilizerCost: Double,
    val laborCost: Double,
    val fuelCost: Double,
    val expectedYield: Double,
    val buyerPrice: Double,
    val currency: String,
    val createdAt: Long
) {
    fun toDomainModel(): CropCycle = CropCycle(
        id = id,
        name = name,
        cropType = cropType,
        seedsCost = seedsCost,
        fertilizerCost = fertilizerCost,
        laborCost = laborCost,
        fuelCost = fuelCost,
        expectedYield = expectedYield,
        buyerPrice = buyerPrice,
        currency = currency,
        createdAt = createdAt
    )

    companion object {
        fun fromDomainModel(cropCycle: CropCycle): CropCycleEntity = CropCycleEntity(
            id = cropCycle.id,
            name = cropCycle.name,
            cropType = cropCycle.cropType,
            seedsCost = cropCycle.seedsCost,
            fertilizerCost = cropCycle.fertilizerCost,
            laborCost = cropCycle.laborCost,
            fuelCost = cropCycle.fuelCost,
            expectedYield = cropCycle.expectedYield,
            buyerPrice = cropCycle.buyerPrice,
            currency = cropCycle.currency,
            createdAt = cropCycle.createdAt
        )
    }
}
