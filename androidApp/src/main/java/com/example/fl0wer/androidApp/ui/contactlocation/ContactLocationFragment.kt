package com.example.fl0wer.androidApp.ui.contactlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.di.core.ViewModelFactory
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.util.Const.BUNDLE_INITIAL_ARGS
import com.example.fl0wer.databinding.FragmentContactLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

private const val CAMERA_ZOOM = 10F

class ContactLocationFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ContactLocationViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentContactLocationBinding
    private var mapFragment: SupportMapFragment? = null
    private var mapMarker: Marker? = null

    companion object {
        fun newInstance(params: ContactLocationScreenParams) = ContactLocationFragment().apply {
            arguments = bundleOf(BUNDLE_INITIAL_ARGS to params)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentContactLocationBinding.inflate(inflater, container, false)
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
        mapFragment?.getMapAsync {
            it.setOnMapLongClickListener { location ->
                viewModel.mapLongClicked(location)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateState(it)
            }
        }
    }

    fun initialArguments(): ContactLocationScreenParams {
        arguments?.getParcelable<ContactLocationScreenParams>(BUNDLE_INITIAL_ARGS)
            ?.also { return it }
        throw IllegalArgumentException("Fragment doesn't contain initial args")
    }

    private fun updateState(state: UiState) {
        when (state) {
            is ContactLocationState.Loading -> drawLoadingState()
            is ContactLocationState.Idle -> drawIdleState(state)
        }
    }

    private fun drawLoadingState() = with(binding) {
        toolbar.title = getString(R.string.loading)
    }

    private fun drawIdleState(state: ContactLocationState.Idle) = with(binding) {
        toolbar.title = getString(R.string.contact_location)
        state.location?.apply {
            mapFragment?.getMapAsync {
                val location = LatLng(latitude, longitude)
                mapMarker?.remove()
                mapMarker = it.addMarker {
                    position(location)
                }
                if (state.firstEntryZoom) {
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(location, CAMERA_ZOOM))
                }
            }
        }
    }
}
