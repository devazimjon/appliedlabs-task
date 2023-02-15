package me.demo.yandexsimulator.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.demo.yandexsimulator.data.database.AppDatabase
import me.demo.yandexsimulator.domain.MapState
import me.demo.yandexsimulator.domain.model.LocationPoint
import me.demo.yandexsimulator.domain.model.toEntity
import javax.inject.Inject

class MapSharedViewModel @Inject constructor(
    private val database: AppDatabase
) : ViewModel() {

    var mapState: MutableLiveData<MapState> = MutableLiveData(MapState.IDLE)

    private var _fromPoint: MutableLiveData<LocationPoint> = MutableLiveData()
    val fromPoint: LiveData<LocationPoint> = _fromPoint

    private var _toPoint: MutableLiveData<LocationPoint> = MutableLiveData()
    val toPoint: LiveData<LocationPoint> = _toPoint

    fun actionButtonSelected() {
        when (mapState.value!!) {
            MapState.IDLE -> {
                mapState.value = MapState.PICK_FROM_LOCATION
            }
            MapState.PICK_FROM_LOCATION -> {
                mapState.value = MapState.PICK_TO_LOCATION
            }
            MapState.PICK_TO_LOCATION -> {
                mapState.value = MapState.ROUTE
            }
            MapState.ROUTE -> {

            }
        }
    }

    fun onPointChangedInSearch(point: LocationPoint) {
        when (mapState.value!!) {
            MapState.PICK_FROM_LOCATION -> {
                _fromPoint.value = point
            }
            MapState.PICK_TO_LOCATION -> {
                _toPoint.value = point
            }
        }

        //Add result also to database
        viewModelScope.launch {
            database.favoriteAddressDao.insert(point.toEntity())
        }
    }

    fun onPointSelectedInMap(locationPoint: LocationPoint) {
        when (mapState.value!!) {
            MapState.PICK_FROM_LOCATION -> {
                _fromPoint.value = locationPoint
                actionButtonSelected()
            }
            MapState.PICK_TO_LOCATION -> {
                _toPoint.value = locationPoint
                actionButtonSelected()
            }
            else -> {
            }
        }

    }
}