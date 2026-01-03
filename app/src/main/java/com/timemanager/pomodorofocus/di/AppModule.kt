package com.timemanager.pomodorofocus.di

import com.timemanager.pomodorofocus.data.repository.SchedulerRepositoryImpl
import com.timemanager.pomodorofocus.data.repository.SettingsRepositoryImpl
import com.timemanager.pomodorofocus.domain.settings.SettingsRepository
import com.timemanager.pomodorofocus.domain.timer.SchedulerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindSchedulerRepository(
        impl: SchedulerRepositoryImpl
    ): SchedulerRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
