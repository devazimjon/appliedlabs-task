package me.demo.yandexsimulator.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import me.demo.yandexsimulator.domain.model.LocationPoint

@Entity(tableName = "favorite_addresses")
data class FavoriteAddressEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val placeId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val formattedAddress: String
)

fun FavoriteAddressEntity.toLocationPoint() = LocationPoint(
    placeId = placeId,
    latLng = LatLng(
        latitude,
        longitude
    ),
    formattedAddress = formattedAddress,
    name = name
)