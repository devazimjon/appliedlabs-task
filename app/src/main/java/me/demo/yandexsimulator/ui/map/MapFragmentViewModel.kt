package me.demo.yandexsimulator.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.demo.yandexsimulator.data.repository.repositories.GoogleRepository
import me.demo.yandexsimulator.domain.MapState
import me.demo.yandexsimulator.domain.model.LocationPoint
import me.demo.yandexsimulator.utils.mvvm.Loading
import me.demo.yandexsimulator.utils.mvvm.Success
import me.demo.yandexsimulator.utils.mvvm.UIState
import javax.inject.Inject


class MapFragmentViewModel @Inject constructor(
    private val googleApiRepository: GoogleRepository
) : ViewModel() {

    var initialPoint : LatLng? = null

    var locationPoints: MutableMap<String, Marker?> = HashMap()

    private var _currentLocation: MutableLiveData<UIState<LocationPoint>> = MutableLiveData()
    val currentLocation: LiveData<UIState<LocationPoint>> = _currentLocation

    fun onPointChangedInMap(latLng: LatLng, mapState: MapState) = viewModelScope.launch {
        when (mapState) {
            MapState.PICK_FROM_LOCATION,
            MapState.PICK_TO_LOCATION -> {
                _currentLocation.postValue(Loading())
                googleApiRepository.reverseDecode(latLng)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        _currentLocation.postValue(Success(it))
                    }
            }
            else -> {
            }
        }

    }


    private fun isGooglePlayServicesAvailable(context: Context): Boolean {
        // Check that Google Play services is available
        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        return ConnectionResult.SUCCESS == resultCode
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun checkPermissions(context: Context): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // Request permissions if not granted before
    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    companion object {
        const val LOCATION_FROM = "from"
        const val LOCATION_TO = "to"
        const val PERMISSION_ID = 1
    }
}