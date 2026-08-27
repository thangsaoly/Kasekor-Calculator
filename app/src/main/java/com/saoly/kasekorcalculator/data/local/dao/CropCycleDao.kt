package com.saoly.kasekorcalculator.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saoly.kasekorcalculator.data.local.entity.CropCycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CropCycleDao {
    @Query("SELECT * FROM crop_cycles ORDER BY createdAt DESC")
    fun getAllCropCycles(): Flow<List<CropCycleEntity>>

    @Query("SELECT * FROM crop_cycles WHERE id = :id")
    fun getCropCycleById(id: Int): Flow<CropCycleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCropCycle(cropCycle: CropCycleEntity)

    @Delete
    suspend fun deleteCropCycle(cropCycle: CropCycleEntity)
}
