package com.saoly.kasekorcalculator.di

import android.content.Context
import androidx.room.Room
import com.saoly.kasekorcalculator.data.local.KasekorDatabase
import com.saoly.kasekorcalculator.data.local.dao.CropCycleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KasekorDatabase =
        Room.databaseBuilder(
            context,
            KasekorDatabase::class.java,
            "kasekor_database"
        )
            .addMigrations(KasekorDatabase.MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideCropCycleDao(database: KasekorDatabase): CropCycleDao = database.cropCycleDao
}
