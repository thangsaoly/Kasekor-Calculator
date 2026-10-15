package com.saoly.kasekorcalculator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saoly.kasekorcalculator.domain.model.CropCycle
import com.saoly.kasekorcalculator.domain.model.Currency
import com.saoly.kasekorcalculator.domain.model.YieldUnit

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
    val yieldUnit: String,
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
        currency = Currency.fromCode(currency),
        yieldUnit = YieldUnit.fromCode(yieldUnit),
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
            currency = cropCycle.currency.name,
            yieldUnit = cropCycle.yieldUnit.name,
            createdAt = cropCycle.createdAt
        )
    }
}
