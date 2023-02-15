package me.demo.yandexsimulator.utils

import com.google.android.gms.maps.model.LatLng

fun LatLng.stringFormat() : String =
    "$latitude, $longitude"