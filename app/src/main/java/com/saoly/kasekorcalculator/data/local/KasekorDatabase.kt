package com.saoly.kasekorcalculator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saoly.kasekorcalculator.data.local.dao.CropCycleDao
import com.saoly.kasekorcalculator.data.local.entity.CropCycleEntity

@Database(entities = [CropCycleEntity::class], version = 1, exportSchema = false)
abstract class KasekorDatabase : RoomDatabase() {
    abstract val cropCycleDao: CropCycleDao

    companion object {
        @Volatile
        private var INSTANCE: KasekorDatabase? = null

        fun getDatabase(context: Context): KasekorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KasekorDatabase::class.java,
                    "kasekor_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
