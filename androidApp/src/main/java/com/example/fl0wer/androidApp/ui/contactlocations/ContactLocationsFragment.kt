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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject
import kotlinx.coroutines.flow.collect


class ContactLocationsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ContactLocationsViewModelFactory
    private val viewModel: ContactLocationsViewModel by viewModels {
        ContactLocationsViewModel.provideFactory(viewModelFactory)
    }
    private lateinit var binding: FragmentContactLocationsBinding

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync {
            it.addMarker(
                MarkerOptions()
                    .position(LatLng(0.0, 0.0))
                    .title("Marker")
            )
            it.addMarker(
                MarkerOptions()
                    .position(LatLng(10.0, 10.0))
                    .title("Marker")
                )
        }
    }

    private fun updateState(state: UiState) {
        when (state) {
            is ContactLocationsState.Idle -> drawIdleState(state)
            is ContactLocationsState.Loading -> drawLoadingState()
            is ContactLocationsState.Failure -> drawFailureState()
        }
    }

    private fun drawIdleState(state: ContactLocationsState.Idle) = with(binding) {
    }

    private fun drawLoadingState() = with(binding) {
    }

    private fun drawFailureState() = with(binding) {
    }
}
