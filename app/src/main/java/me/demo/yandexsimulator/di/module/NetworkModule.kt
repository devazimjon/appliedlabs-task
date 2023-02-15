package me.demo.yandexsimulator.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import me.demo.yandexsimulator.common.util.Constants
import me.demo.yandexsimulator.data.network.rest.adapter.FlowCallAdapterFactory
import me.demo.yandexsimulator.data.network.rest.converter.CustomConvertersFactory
import me.demo.yandexsimulator.data.remote.service.GoogleApiService
import me.demo.yandexsimulator.di.annotation.AppScope
import me.demo.yandexsimulator.utils.serialization.actual
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [InterceptorModule::class])
object NetworkModule {

    @Provides
    @AppScope
    fun provideService(retrofit: Retrofit): GoogleApiService {
        return retrofit.create(GoogleApiService::class.java)
    }

    @Provides
    @AppScope
    fun providesRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_GOOGLE_GEOCODER_URL)
            .addCallAdapterFactory(FlowCallAdapterFactory)
            .addConverterFactory(CustomConvertersFactory)
            .addConverterFactory(Json.actual.asConverterFactory("application/json; ".toMediaType()))
            .client(okHttpClient)
            .build()
    }
}