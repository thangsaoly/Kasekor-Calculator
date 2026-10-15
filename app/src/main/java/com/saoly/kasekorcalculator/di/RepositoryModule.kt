package com.saoly.kasekorcalculator.di

import com.saoly.kasekorcalculator.data.repository.CropRepositoryImpl
import com.saoly.kasekorcalculator.domain.repository.CropRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCropRepository(impl: CropRepositoryImpl): CropRepository
}
