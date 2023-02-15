package me.demo.yandexsimulator.data.mapper

import com.google.android.gms.maps.model.LatLng
import me.demo.yandexsimulator.data.model.response.LocationDecodeResponse
import me.demo.yandexsimulator.domain.model.LocationPoint

fun LocationDecodeResponse.toLocationPointList(): List<LocationPoint> {
    val list = arrayListOf<LocationPoint>()

    results.forEach {
        list.add(
            LocationPoint(
                placeId = it.placeId,
                latLng = LatLng(
                    it.geometry.latLng.lat,
                    it.geometry.latLng.lng
                ),
                formattedAddress = it.formattedAddress,
                name = it.name
            )
        )
    }

    return list
}