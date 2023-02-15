package me.demo.yandexsimulator.domain.model

import com.google.android.gms.maps.model.LatLng
import me.demo.yandexsimulator.data.database.model.FavoriteAddressEntity

data class LocationPoint(
    val placeId: String,
    val latLng: LatLng,
    val formattedAddress: String = "",
    val name: String = "",
)

fun LocationPoint.toEntity() = FavoriteAddressEntity(
    placeId = placeId,
    latitude = latLng.latitude,
    longitude = latLng.longitude,
    name = name,
    formattedAddress = formattedAddress
)