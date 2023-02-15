package me.demo.yandexsimulator.di.module

import dagger.Binds
import dagger.Module
import me.demo.yandexsimulator.data.repository.impl.GoogleRepositoryImpl
import me.demo.yandexsimulator.data.repository.repositories.GoogleRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindGoogleRepository(googleRepository: GoogleRepositoryImpl): GoogleRepository
}