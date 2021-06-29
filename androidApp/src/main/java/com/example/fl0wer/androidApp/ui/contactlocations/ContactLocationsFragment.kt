package com.example.fl0wer.androidApp.ui.contactlocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.androidApp.di.core.ViewModelFactory
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.databinding.FragmentContactLocationsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

private const val CAMERA_BOUNDS_PADDING = 100

class ContactLocationsFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ContactLocationsViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentContactLocationsBinding
    private var mapFragment: SupportMapFragment? = null

    companion object {
        fun newInstance() = ContactLocationsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentContactLocationsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                viewModel.backPressed()
            }
        }
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateState(it)
            }
        }
    }

    private fun updateState(state: UiState) {
        when (state) {
            is ContactLocationsState.Loading -> drawLoadingState()
            is ContactLocationsState.Idle -> drawIdleState(state)
        }
    }

    private fun drawLoadingState() = with(binding) {
        toolbar.title = getString(R.string.loading)
    }

    private fun drawIdleState(state: ContactLocationsState.Idle) = with(binding) {
        toolbar.title = getString(R.string.contacts_locations)
        mapFragment?.getMapAsync {
            it.showMarkers(state.locations)
        }
    }

    private fun GoogleMap.showMarkers(locations: List<LocationParcelable>) {
        if (locations.isEmpty()) {
            return
        }
        val boundsBuilder = LatLngBounds.Builder()
        locations.forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            boundsBuilder.include(latLng)
            addMarker {
                position(latLng)
            }
        }
        moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                boundsBuilder.build(),
                CAMERA_BOUNDS_PADDING,
            )
        )
    }
}
