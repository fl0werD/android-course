package com.example.fl0wer.androidApp.ui.contactlocations

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.di.App
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.databinding.FragmentContactLocationsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class ContactLocationsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ContactLocationsViewModelFactory
    private val viewModel: ContactLocationsViewModel by viewModels {
        ContactLocationsViewModel.provideFactory(viewModelFactory)
    }
    private lateinit var binding: FragmentContactLocationsBinding
    private var mapFragment: SupportMapFragment? = null

    companion object {
        fun newInstance() = ContactLocationsFragment()
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
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
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync {
            val boundsBuilder = LatLngBounds.Builder()
            state.locations.forEach { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                boundsBuilder.include(latLng)
                it.addMarker(
                    MarkerOptions()
                        .position(latLng)
                )
            }
            it.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
        }
    }
}
