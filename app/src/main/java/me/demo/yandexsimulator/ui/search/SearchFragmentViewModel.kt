package me.demo.yandexsimulator.ui.search

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import me.demo.yandexsimulator.data.repository.repositories.GoogleRepository
import me.demo.yandexsimulator.domain.model.LocationPoint
import me.demo.yandexsimulator.utils.mvvm.SingleLiveEvent
import javax.inject.Inject

class SearchFragmentViewModel @Inject constructor(
    private val googleApiRepository: GoogleRepository
) : ViewModel() {

    private var _loadingState: SingleLiveEvent<Unit> = SingleLiveEvent()
    val loadingState: SingleLiveEvent<Unit> = _loadingState

    private val _currentActiveField: MutableLiveData<LocationField> =
        MutableLiveData(LocationField.NONE)
    var currentActiveField: LiveData<LocationField> = _currentActiveField

    private val _currentQuery: MutableLiveData<String> = MutableLiveData("")
    var currentQuery: LiveData<String> = _currentQuery

    val searchResults =
        _currentQuery.asFlow()
            .flatMapLatest { search ->
                if (search.isNotBlank()){
                    _loadingState.postValue(Unit)
                    googleApiRepository.searchLocation(search)
                }
                else
                    flowOf(emptyList())
            }
            .catch {
            }
            .asLiveData()

    fun setCurrentActiveField(field: LocationField) {
        _currentActiveField.value = field
    }

    fun submitQuery(query: String) {
        if (_currentQuery.value != query && _currentActiveField.value != LocationField.NONE) {
            _currentQuery.value = query
        }
    }

    fun decodeAddressSelected(point: LocationPoint) {

    }

    enum class LocationField {
        LOCATION_FROM,
        LOCATION_TO,
        NONE
    }
}