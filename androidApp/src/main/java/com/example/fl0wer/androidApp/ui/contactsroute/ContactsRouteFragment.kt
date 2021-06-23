package com.example.fl0wer.androidApp.ui.contactsroute

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.directions.DirectionParcelable
import com.example.fl0wer.androidApp.di.App
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.databinding.FragmentContactLocationsBinding
import com.example.fl0wer.databinding.FragmentContactsRouteBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

class ContactsRouteFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ContactsRouteViewModelFactory
    private val viewModel: ContactsRouteViewModel by viewModels {
        ContactsRouteViewModel.provideFactory(
            viewModelFactory,
            arguments?.getInt(ARGUMENT_START_CONTACT_ID, -1) ?: -1,
            arguments?.getInt(ARGUMENT_END_CONTACT_ID, -1) ?: -1,
        )
    }
    private lateinit var binding: FragmentContactsRouteBinding
    private var mapFragment: SupportMapFragment? = null

    companion object {
        private const val ARGUMENT_START_CONTACT_ID = "ARGUMENT_START_CONTACT_ID"
        private const val ARGUMENT_END_CONTACT_ID = "ARGUMENT_END_CONTACT_ID"

        fun newInstance(startContactId: Int, endContactId: Int) = ContactsRouteFragment().apply {
            arguments = bundleOf(
                ARGUMENT_START_CONTACT_ID to startContactId,
                ARGUMENT_END_CONTACT_ID to endContactId,
            )
        }
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentContactsRouteBinding.inflate(inflater, container, false)
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
            is ContactsRouteState.Loading -> drawLoadingState()
            is ContactsRouteState.Idle -> drawIdleState(state)
            is ContactsRouteState.RouteNotFound -> drawRouteNotFound()
        }
    }

    private fun drawLoadingState() = with(binding) {
        toolbar.title = getString(R.string.loading)
    }

    private fun drawIdleState(state: ContactsRouteState.Idle) = with(binding) {
        toolbar.title = getString(R.string.route)
        mapFragment?.getMapAsync {
            it.addPolyline(PolylineOptions().addAll(state.route))
        }
    }

    private fun drawRouteNotFound() = with(binding) {
        toolbar.title = getString(R.string.route_not_found)
    }
}
