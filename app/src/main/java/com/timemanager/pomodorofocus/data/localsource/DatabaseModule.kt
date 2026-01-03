package com.timemanager.pomodorofocus.data.localsource

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDatabase(
        context: Context,
        callback: DatabaseCallback
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "pomodoro_database"
        )
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideTimerDao(database: AppDatabase): TimerDao {
        return database.timerDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(timerDaoProvider: Provider<TimerDao>): DatabaseCallback {
        return DatabaseCallback(timerDaoProvider)
    }
}
