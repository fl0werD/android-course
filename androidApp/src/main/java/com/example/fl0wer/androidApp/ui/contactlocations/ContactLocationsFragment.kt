package com.example.fl0wer.androidApp.ui.contactlocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.androidApp.ui.core.BaseFragment
import com.example.fl0wer.databinding.FragmentContactLocationsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import kotlinx.coroutines.flow.collect

private const val CAMERA_BOUNDS_PADDING = 100

class ContactLocationsFragment : BaseFragment<ContactLocationsViewModel, ContactLocationsState>() {
    override val vmClass = ContactLocationsViewModel::class.java
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
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.apply {
            getMapAsync {
                it.uiSettings.isCompassEnabled = false
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.uiState().collect {
                updateState(it)
            }
        }
    }

    override fun updateState(state: ContactLocationsState) {
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
