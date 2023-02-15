package me.demo.yandexsimulator.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import me.demo.yandexsimulator.data.database.AppDatabase
import me.demo.yandexsimulator.di.annotation.AppScope

@Module
object DatabaseModule {

    @AppScope
    @Provides
    internal fun provideAppDatabase(context: Application): AppDatabase {
        return AppDatabase.create(context)
    }
}