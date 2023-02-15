package me.demo.yandexsimulator.data.remote.service

import me.demo.yandexsimulator.data.model.response.LocationDecodeResponse
import me.demo.yandexsimulator.data.model.response.ReverseDecodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApiService {

    @GET("geocode/json")
    suspend fun decodeLocation(
        @Query("address") address: String,
        @Query("key") key: String
    ): Response<LocationDecodeResponse>

    @GET("geocode/json")
    suspend fun reverseDecodePoint(
        @Query("latlng") latlng: String,
        @Query("key") key: String
    ): Response<ReverseDecodeResponse>

    @GET("place/textsearch/json")
    suspend fun searchLocation(
        @Query("query") query: String,
        @Query("key") key: String
    ): Response<LocationDecodeResponse>
}