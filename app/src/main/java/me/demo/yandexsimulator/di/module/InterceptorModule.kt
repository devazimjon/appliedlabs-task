package me.demo.yandexsimulator.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import me.demo.yandexsimulator.di.annotation.AppScope
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

@Module
object InterceptorModule {

    @Provides
    @AppScope
    internal fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
        context: Context
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool())
            .addNetworkInterceptor(loggingInterceptor)
            .cache(cache)
            .build()

    @Provides
    @AppScope
    internal fun loggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @AppScope
    fun provideCache(file: File): Cache {
        return Cache(file, 1000 * 1000 * 1000) //1000mb
    }

    @Provides
    @AppScope
    fun provideFile(context: Context): File {
        val file = File(context.cacheDir, "apiCache")
        file.mkdir()
        return file
    }
}