package com.saoly.kasekorcalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saoly.kasekorcalculator.data.local.dao.CropCycleDao
import com.saoly.kasekorcalculator.data.local.entity.CropCycleEntity

@Database(entities = [CropCycleEntity::class], version = 1, exportSchema = false)
abstract class KasekorDatabase : RoomDatabase() {
    abstract val cropCycleDao: CropCycleDao
}
