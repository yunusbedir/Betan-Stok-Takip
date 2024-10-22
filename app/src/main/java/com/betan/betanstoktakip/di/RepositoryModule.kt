package com.betan.betanstoktakip.di

import com.betan.betanstoktakip.data.local.LocalRepository
import com.betan.betanstoktakip.data.local.LocalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindLocalRepository(repository: LocalRepositoryImpl): LocalRepository
}