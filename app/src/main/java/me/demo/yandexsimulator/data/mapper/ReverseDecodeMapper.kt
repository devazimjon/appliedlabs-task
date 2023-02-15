package me.demo.yandexsimulator.data.mapper

import com.google.android.gms.maps.model.LatLng
import me.demo.yandexsimulator.data.model.response.ReverseDecodeResponse
import me.demo.yandexsimulator.domain.model.LocationPoint

fun ReverseDecodeResponse.toLocationPoint(): LocationPoint? =
    if (results.isNotEmpty()) {
        LocationPoint(
            placeId = results[0].placeId,
            latLng = LatLng(
                results[0].geometry.latLng.lat,
                results[0].geometry.latLng.lng
            ),
            formattedAddress = results[0].formattedAddress
        )
    } else null

