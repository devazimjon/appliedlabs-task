package me.demo.yandexsimulator.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDecodeResponse(
    @SerialName("results")
    val results: List<LocationResponse>,
    @SerialName("status")
    val status: String
)

@Serializable
data class LocationResponse(
    @SerialName("formatted_address")
    val formattedAddress: String,
    @SerialName("name")
    val name: String,
    @SerialName("geometry")
    val geometry: GeometryResponse,
    @SerialName("place_id")
    val placeId: String
)