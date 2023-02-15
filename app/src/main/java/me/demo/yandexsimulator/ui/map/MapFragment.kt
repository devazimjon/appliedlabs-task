package me.demo.yandexsimulator.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import me.demo.yandexsimulator.R
import me.demo.yandexsimulator.databinding.FragmentMapBinding
import me.demo.yandexsimulator.di.AppComponent
import me.demo.yandexsimulator.domain.MapState
import me.demo.yandexsimulator.ui.map.MapFragmentViewModel.Companion.LOCATION_FROM
import me.demo.yandexsimulator.ui.map.MapFragmentViewModel.Companion.LOCATION_TO
import me.demo.yandexsimulator.ui.shared.MapSharedViewModel
import me.demo.yandexsimulator.utils.mvvm.Loading
import me.demo.yandexsimulator.utils.mvvm.Success
import me.demo.yandexsimulator.utils.navigate
import javax.inject.Inject


class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val activityViewModel by activityViewModels<MapSharedViewModel> { factory }
    private val viewModel by viewModels<MapFragmentViewModel> { factory }

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var mMap: GoogleMap? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationListener: LocationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationListener =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        configureInitialState()
        initUI()
        initVM()
    }

    private fun configureInitialState() {
        when (activityViewModel.mapState.value!!) {
            MapState.PICK_FROM_LOCATION -> {
                activityViewModel.fromPoint.value?.let {
                    viewModel.initialPoint = it.latLng
                }
            }
            MapState.PICK_TO_LOCATION -> {
                activityViewModel.toPoint.value?.let {
                    viewModel.initialPoint = it.latLng
                }
            }
        }
    }

    private fun initUI() {
        binding.searchBar.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToSearchFragment()
            navigate(action)
        }

        binding.actionBtn.setOnClickListener {
            if (activityViewModel.mapState.value!! == MapState.IDLE)
                activityViewModel.actionButtonSelected()
            else
                viewModel.currentLocation.value?.let {
                    if (it is Success) {
                        activityViewModel.onPointSelectedInMap(it.data)
                    }
                }
        }

        binding.myLocationFab.setOnClickListener {
            getLastLocation()
        }
    }

    private fun initVM() {
        activityViewModel.mapState.observe(viewLifecycleOwner) {
            updateState(it)
        }

        activityViewModel.fromPoint.observe(viewLifecycleOwner) {
            binding.root.postDelayed({
                viewModel.locationPoints[LOCATION_FROM]?.remove()
                val options = MarkerOptions()
                    .position(it.latLng)
                val marker = mMap?.addMarker(options)
                viewModel.locationPoints[LOCATION_FROM] = marker
            }, 100)
        }

        activityViewModel.toPoint.observe(viewLifecycleOwner) {
            binding.root.postDelayed({
                viewModel.locationPoints[LOCATION_TO]?.remove()
                val options = MarkerOptions()
                    .position(it.latLng)
                val marker = mMap?.addMarker(options)
                viewModel.locationPoints[LOCATION_TO] = marker
            }, 100)

        }


        viewModel.currentLocation.observe(viewLifecycleOwner) {
            when (it) {
                is Loading -> {
                    binding.locationInfo.isInLoadingState = true
                }
                is Success -> {
                    binding.locationInfo.isInLoadingState = false
                    binding.locationInfo.locationNameText = it.data.formattedAddress
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        val initialPoint = viewModel.initialPoint
        if (initialPoint != null)
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPoint, defaultZoom))
        else
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(tashkent, defaultZoom))

        enableMyLocation()
        mMap?.setOnCameraIdleListener {
            mMap?.cameraPosition?.let {
                viewModel.onPointChangedInMap(it.target, activityViewModel.mapState.value!!)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (viewModel.checkPermissions(requireContext())) {
            mMap?.isMyLocationEnabled = true
        } else {
            viewModel.requestPermissions(requireActivity())
        }
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, defaultPickingZoom)
        mMap?.animateCamera(cameraUpdate)
        locationListener?.removeUpdates(this)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (viewModel.checkPermissions(requireContext())) {
            if (viewModel.isLocationEnabled(requireContext())) {

                mFusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    val location: Location = task.result
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocation,
                            defaultPickingZoom
                        )
                    )
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                requireContext().startActivity(intent)
            }
        } else {
            viewModel.requestPermissions(requireActivity())
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val mLastLocation: Location = p0.lastLocation
        }
    }

    private fun updateState(mapState: MapState) {
        when (mapState) {
            MapState.IDLE -> {
                binding.locationInfo.isVisible = false
                binding.actionBtn.isVisible = true
                binding.actionBtn.text = getString(R.string.start_button)
            }
            MapState.PICK_FROM_LOCATION -> {
                binding.locationInfo.isVisible = true
                binding.locationInfo.statusText = getString(R.string.from_location)
                binding.actionBtn.text = getString(R.string.next)
                invalidateMap()
            }
            MapState.PICK_TO_LOCATION -> {
                binding.locationInfo.isVisible = true
                binding.locationInfo.statusText = getString(R.string.to_location)
                binding.actionBtn.text = getString(R.string.next)
                invalidateMap()
            }
            MapState.ROUTE -> {
                binding.actionBtn.isVisible = false
                binding.locationInfo.isVisible = false
                invalidateMap(resultZoom)
            }
        }
    }

    private fun invalidateMap(withZoom: Float = defaultPickingZoom) {
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(tashkent, withZoom))
    }

    //MARK - Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.get().inject(this)
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationListener?.removeUpdates(this)
        locationListener = null
        mFusedLocationClient = null
        binding.mapView.onDestroy()
        mMap = null
        _binding = null
    }

    companion object {
        val tashkent = LatLng(41.287501, 69.228978)
        const val defaultZoom = 13f
        const val defaultPickingZoom = 15f
        const val resultZoom = 12f
    }
}