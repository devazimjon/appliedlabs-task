package me.demo.yandexsimulator.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReverseDecodeResponse(
    @SerialName("results")
    val results: List<AddressResponse>,
    @SerialName("status")
    val status: String
)

@Serializable
data class AddressResponse(
    @SerialName("formatted_address")
    val formattedAddress: String,
    @SerialName("geometry")
    val geometry: GeometryResponse,
    @SerialName("place_id")
    val placeId: String
)

@Serializable
data class GeometryResponse(
    @SerialName("location")
    val latLng: LatLngResponse,
)

@Serializable
 data class LatLngResponse(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double,
)