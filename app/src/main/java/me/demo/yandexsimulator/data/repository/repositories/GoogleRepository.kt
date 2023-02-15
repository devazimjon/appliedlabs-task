package me.demo.yandexsimulator.data.repository.repositories

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import me.demo.yandexsimulator.data.model.response.LocationDecodeResponse
import me.demo.yandexsimulator.domain.model.LocationPoint

interface GoogleRepository {

    suspend fun decodeLocation(location: String): Flow<LocationDecodeResponse>

    suspend fun reverseDecode(latLng: LatLng): Flow<LocationPoint>

    suspend fun searchLocation(query: String): Flow<List<LocationPoint>>
}