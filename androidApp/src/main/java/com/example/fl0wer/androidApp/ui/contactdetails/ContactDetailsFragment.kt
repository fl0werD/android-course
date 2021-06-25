package com.example.fl0wer.androidApp.ui.contactdetails

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.di.App
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.databinding.FragmentContactDetailsBinding
import com.example.fl0wer.databinding.IncludeContactDetailBinding
import com.example.fl0wer.databinding.IncludeContactDetailMultilineBinding
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
            birthdayReminder.setOnClickListener {
                viewModel.changeBirthdayReminder()
            }
            address.root.setOnClickListener {
                viewModel.addressClicked()
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
        val addressValue = state.location?.address ?: getString(R.string.not_specified)
        loadingBar.isVisible = false
        scrollView.isVisible = true
        toolbar.title = contact.name
        if (contact.photo == 0) {
            photo.setImageResource(R.drawable.ic_contact)
        } else {
            photo.setImageResource(contact.photo)
        }
        name.text = contact.name
        phone.putDetail(contact.phone, R.string.phone_main, R.drawable.ic_phone)
        phone2.putDetail(contact.phone2, R.string.phone_mobile, R.drawable.ic_phone)
        email.putDetail(contact.email, R.string.email_main, R.drawable.ic_email)
        email2.putDetail(contact.email2, R.string.email_secondary, R.drawable.ic_email)
        note.putDetail(contact.note, R.drawable.ic_note)
        birthdayLayout.putBirthday(contact, state.birthdayReminder)
        address.putDetail(addressValue, R.drawable.ic_location)
    }

    private fun drawLoadingState() = with(binding) {
        loadingBar.isVisible = true
        scrollView.isVisible = false
        toolbar.title = getString(R.string.loading)
    }

    private fun drawFailureState() = with(binding) {
        loadingBar.isVisible = false
        scrollView.isVisible = false
        toolbar.title = getString(R.string.contact_details)
    }

    private fun IncludeContactDetailBinding.putDetail(
        value: String,
        @DrawableRes icon: Int = 0,
    ) {
        if (value.isNotEmpty()) {
            root.isVisible = true
            detailIcon.setImageResource(icon)
            detailValue.text = value
        } else {
            root.isVisible = false
        }
    }

    private fun IncludeContactDetailMultilineBinding.putDetail(
        value: String,
        @StringRes desc: Int,
        @DrawableRes icon: Int = 0,
    ) {
        if (value.isNotEmpty()) {
            root.isVisible = true
            detailIcon.setImageResource(icon)
            detailValue.text = value
            detailDesc.text = getString(desc)
        } else {
            root.isVisible = false
        }
    }

    private fun RelativeLayout.putBirthday(contact: ContactParcelable, reminder: Boolean) =
        with(binding) {
            if (contact.birthdayMonth != -1 && contact.birthdayDayOfMonth != -1) {
                isVisible = true
                birthday.putDetail(
                    getString(
                        R.string.birthday_value,
                        contact.birthdayMonth,
                        contact.birthdayDayOfMonth,
                    ),
                    R.string.birthday,
                    R.drawable.ic_event,
                )
                if (reminder) {
                    birthdayReminder.setImageResource(R.drawable.ic_notifications_active)
                    birthdayReminder.setColorFilter(
                        ResourcesCompat.getColor(resources, R.color.teal_500, null)
                    )
                } else {
                    birthdayReminder.setImageResource(R.drawable.ic_notifications_none)
                    birthdayReminder.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                }
            } else {
                isVisible = false
            }
        }
}
