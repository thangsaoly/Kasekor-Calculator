package com.saoly.kasekorcalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.saoly.kasekorcalculator.data.local.dao.CropCycleDao
import com.saoly.kasekorcalculator.data.local.entity.CropCycleEntity

@Database(entities = [CropCycleEntity::class], version = 2, exportSchema = false)
abstract class KasekorDatabase : RoomDatabase() {
    abstract val cropCycleDao: CropCycleDao

    companion object {
        // v2: yield unit per crop cycle; existing records were entered in kg
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE crop_cycles ADD COLUMN yieldUnit TEXT NOT NULL DEFAULT 'KG'")
            }
        }
    }
}
