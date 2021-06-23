package com.example.fl0wer.androidApp.ui.contactdetails

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.di.App
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.databinding.FragmentContactDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ContactDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ContactDetailsViewModelFactory
    private val viewModel: ContactDetailsViewModel by viewModels {
        ContactDetailsViewModel.provideFactory(
            viewModelFactory,
            arguments?.getString(ARGUMENT_CONTACT_ID) ?: "",
        )
    }
    private lateinit var binding: FragmentContactDetailsBinding
    private var mapFragment: SupportMapFragment? = null

    companion object {
        private const val ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID"

        fun newInstance(contactId: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(ARGUMENT_CONTACT_ID to contactId)
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
    ) = FragmentContactDetailsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                viewModel.backPressed()
            }
            birthdayNotice.setOnClickListener {
                viewModel.changeBirthdayNotice()
            }
        }
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync {
            it.setOnMapLongClickListener { location ->
                viewModel.mapClicked(location)
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
            is ContactDetailsState.Idle -> drawIdleState(state)
            is ContactDetailsState.Loading -> drawLoadingState()
            is ContactDetailsState.Failure -> drawFailureState()
        }
    }

    private fun drawIdleState(state: ContactDetailsState.Idle) = with(binding) {
        val contact = state.contact
        loadingBar.isVisible = false
        scrollView.isVisible = true
        if (contact.photo == 0) {
            photo.setImageResource(R.drawable.ic_contact)
        } else {
            photo.setImageResource(contact.photo)
        }
        name.text = contact.name
        phone.putText(phoneTitle, contact.phone)
        phone2.putText(phone2Title, contact.phone2)
        email.putText(emailTitle, contact.email)
        email2.putText(email2Title, contact.email2)
        note.putText(noteTitle, contact.note)

        if (contact.birthdayMonth != -1 && contact.birthdayDayOfMonth != -1) {
            birthdayTitle.isVisible = true
            birthday.isVisible = true
            birthday.text = getString(
                R.string.birthday_value,
                contact.birthdayMonth,
                contact.birthdayDayOfMonth
            )
            birthdayNotice.isVisible = true
            if (state.birthdayReminder) {
                birthdayNotice.setImageResource(R.drawable.ic_notifications_active)
                birthdayNotice.setColorFilter(R.color.teal_500, PorterDuff.Mode.SRC_IN)
            } else {
                birthdayNotice.setImageResource(R.drawable.ic_notifications_none)
                birthdayNotice.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
            }
        } else {
            birthdayTitle.isVisible = false
            birthday.isVisible = false
            birthdayNotice.isVisible = false
        }

        state.location?.apply {
            mapFragment?.getMapAsync {
                val location = LatLng(latitude, longitude)
                it.addMarker {
                    position(location)
                }
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10F))
            }
        }
    }

    private fun drawLoadingState() = with(binding) {
        loadingBar.isVisible = true
        scrollView.isVisible = false
    }

    private fun drawFailureState() = with(binding) {
        loadingBar.isVisible = false
        scrollView.isVisible = false
    }

    private fun TextView.putText(titleView: TextView, textValue: String) {
        if (textValue.isNotEmpty()) {
            titleView.isVisible = true
            isVisible = true
            text = textValue
        } else {
            titleView.isVisible = false
            isVisible = false
        }
    }
}
