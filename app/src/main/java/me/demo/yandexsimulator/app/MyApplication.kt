package me.demo.yandexsimulator.app

import android.app.Application
import me.demo.yandexsimulator.di.AppComponent

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppComponent.create(this)
    }
}