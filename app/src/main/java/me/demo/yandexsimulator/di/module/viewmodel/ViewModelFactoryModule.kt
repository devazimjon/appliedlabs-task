package me.demo.yandexsimulator.di.module.viewmodel

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import me.demo.yandexsimulator.di.module.viewmodel.factory.ViewModelProvidersFactory

@Module(includes = [ViewModelsModule::class])
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(
        viewModelProvidersFactory: ViewModelProvidersFactory
    ): ViewModelProvider.Factory

}