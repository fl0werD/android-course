package com.example.fl0wer.androidApp.ui.contactsroute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.directions.BoundsParcelable
import com.example.fl0wer.androidApp.di.core.ViewModelFactory
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactsAdapter
import com.example.fl0wer.androidApp.util.Const.BUNDLE_INITIAL_ARGS
import com.example.fl0wer.androidApp.util.toLatLng
import com.example.fl0wer.databinding.FragmentContactsRouteBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

private const val CAMERA_BOUNDS_PADDING = 100

class ContactsRouteFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ContactsRouteViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentContactsRouteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private var mapFragment: SupportMapFragment? = null
    private val contactsAdapter by lazy {
        ContactsAdapter { position ->
            viewModel.contactClicked(position)
        }
    }

    companion object {
        fun newInstance(params: ContactsRouteScreenParams) = ContactsRouteFragment().apply {
            arguments = bundleOf(BUNDLE_INITIAL_ARGS to params)
        }
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
            bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheetCard)
        }
        with(binding.contactsList) {
            layoutManager = LinearLayoutManager(context).apply { recycleChildrenOnDetach = true }
            adapter = contactsAdapter
        }
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateState(it)
            }
        }
    }

    fun initialArguments(): ContactsRouteScreenParams {
        arguments?.getParcelable<ContactsRouteScreenParams>(BUNDLE_INITIAL_ARGS)?.also { return it }
        throw IllegalArgumentException("Fragment doesn't contain initial args")
    }

    private fun updateState(state: ContactsRouteState) = with(binding) {
        if (state.loading) {
            toolbar.title = getString(R.string.loading)
        } else {
            toolbar.title = getString(R.string.route)
            if (state.error != null) {
                Toast.makeText(context, getString(R.string.route_not_found), Toast.LENGTH_LONG).show()
            }
        }
        contactsAdapter.items = state.contacts
        mapFragment?.getMapAsync { map ->
            map.clear()
            val route = state.route
            if (route == null || state.error != null) {
                return@getMapAsync
            }
            map.addPolyline(
                PolylineOptions()
                    .addAll(route.points.map { it.toLatLng() })
            )
            map.animateCameraToBounds(route.bounds)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun GoogleMap.animateCameraToBounds(bounds: BoundsParcelable) {
        val locationBounds = LatLngBounds.Builder()
            .include(bounds.northeast.toLatLng())
            .include(bounds.southwest.toLatLng())
            .build()
        animateCamera(CameraUpdateFactory.newLatLngBounds(locationBounds, CAMERA_BOUNDS_PADDING))
    }
}
