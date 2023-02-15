package me.demo.yandexsimulator.di.module

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import dagger.Module
import dagger.Provides
import me.demo.yandexsimulator.di.annotation.AppScope

@Module
object AppModule {

    @AppScope
    @Provides
    fun bindContext(application: Application): Context = application

    @AppScope
    @Provides
    fun provideMetadataBundle(application: Application): Bundle =
        application.packageManager
            .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA).metaData


}