package me.demo.yandexsimulator.di.module.builder

import me.demo.yandexsimulator.common.util.Connectivity
import me.demo.yandexsimulator.common.util.ConnectivityImpl
import dagger.Binds
import dagger.Module


@Module
abstract class ConnectivityBuilder {

    @Binds
    abstract fun bindConnectivity(connectivityImpl: ConnectivityImpl): Connectivity
}