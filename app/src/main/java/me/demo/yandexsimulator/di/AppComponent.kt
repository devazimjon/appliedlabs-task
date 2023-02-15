package me.demo.yandexsimulator.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import me.demo.yandexsimulator.di.annotation.AppScope
import me.demo.yandexsimulator.di.module.AppModule
import me.demo.yandexsimulator.di.module.DatabaseModule
import me.demo.yandexsimulator.di.module.NetworkModule
import me.demo.yandexsimulator.di.module.RepositoryModule
import me.demo.yandexsimulator.di.module.builder.ConnectivityBuilder
import me.demo.yandexsimulator.di.module.viewmodel.ViewModelFactoryModule
import me.demo.yandexsimulator.ui.map.MapFragment
import me.demo.yandexsimulator.ui.search.SearchFragment

@AppScope
@Component(
    modules = [
        AppModule::class,
        ConnectivityBuilder::class,
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class,
    ]
)
interface AppComponent {

    fun inject(app: Application)
    fun inject(app: MapFragment)
    fun inject(app: SearchFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Application,
        ): AppComponent
    }

    companion object {

        @Volatile
        private var appComponent: AppComponent? = null

        fun get(): AppComponent =
            requireNotNull(appComponent) { "AppComponent is not initialized" }

        fun create(context: Application): AppComponent {
            appComponent?.let { return it }
            synchronized(this) {
                if (appComponent == null)
                    appComponent = DaggerAppComponent.factory().create(context)
            }
            return get()
        }
    }
}