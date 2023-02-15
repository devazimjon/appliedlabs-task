package me.demo.yandexsimulator.data.repository.impl

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.demo.yandexsimulator.data.database.AppDatabase
import me.demo.yandexsimulator.data.database.model.toLocationPoint
import me.demo.yandexsimulator.data.mapper.toLocationPoint
import me.demo.yandexsimulator.data.mapper.toLocationPointList
import me.demo.yandexsimulator.data.model.response.LocationDecodeResponse
import me.demo.yandexsimulator.data.remote.service.GoogleApiService
import me.demo.yandexsimulator.data.repository.repositories.GoogleRepository
import me.demo.yandexsimulator.domain.model.LocationPoint
import me.demo.yandexsimulator.domain.model.getOptionalData
import me.demo.yandexsimulator.domain.model.onFailure
import me.demo.yandexsimulator.domain.model.onSuccess
import me.demo.yandexsimulator.utils.stringFormat
import javax.inject.Inject


class GoogleRepositoryImpl @Inject constructor(
    private val service: GoogleApiService,
    private val metadataBundle: Bundle,
    private val database: AppDatabase
) : GoogleRepository {

    override suspend fun decodeLocation(location: String): Flow<LocationDecodeResponse> = flow {
        val googleApiKey = metadataBundle.getString("com.google.android.geo.API_KEY") ?: ""

        service.decodeLocation(location, googleApiKey)
            .getOptionalData().onSuccess {
                emit(it)
            }.onFailure {
                // skip error
            }
    }

    override suspend fun reverseDecode(latLng: LatLng): Flow<LocationPoint> = flow {
        val googleApiKey = metadataBundle.getString("com.google.android.geo.API_KEY") ?: ""

        service.reverseDecodePoint(latLng.stringFormat(), googleApiKey)
            .getOptionalData().onSuccess {
                it.toLocationPoint()?.let {
                    emit(it)
                }
            }.onFailure {
                // skip error
            }
    }

    override suspend fun searchLocation(query: String): Flow<List<LocationPoint>> = flow {
        val googleApiKey = metadataBundle.getString("com.google.android.geo.API_KEY") ?: ""

        service.searchLocation(query, googleApiKey)
            .getOptionalData().onSuccess {
                val result = it.toLocationPointList()
                if (result.isEmpty())
                    emit(
                        database.favoriteAddressDao.getLast3Addresses()
                            .map { it.toLocationPoint() }
                    )
                else
                    emit(result)
            }.onFailure {
                // skip error
                emit(
                    database.favoriteAddressDao.getLast3Addresses()
                        .map { it.toLocationPoint() }
                )
            }
    }

}