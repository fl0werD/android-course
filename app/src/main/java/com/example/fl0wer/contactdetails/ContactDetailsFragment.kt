package com.example.fl0wer.contactdetails

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fl0wer.R
import com.example.fl0wer.databinding.FragmentContactDetailsBinding
import com.example.fl0wer.repository.ContactsRepository
import java.text.SimpleDateFormat
import java.util.*

class ContactDetailsFragment : Fragment(
    R.layout.fragment_contact_details
) {
    private val binding: FragmentContactDetailsBinding by viewBinding()
    private val viewModelFactory by lazy {
        ContactDetailsViewModelFactory(
            ContactsRepository.get(requireContext()),
            arguments?.getString(ARGUMENT_CONTACT_ID) ?: ""
        )
    }
    private val viewModel: ContactDetailsViewModel by viewModels {
        viewModelFactory
    }

    companion object {
        private const val ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID"
        private const val BIRTHDAY_DATE_FORMAT = "dd.MM.yyyy"

        fun newInstance(
            contactId: String,
        ): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARGUMENT_CONTACT_ID to contactId,
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getScreenState().observe(viewLifecycleOwner, {
            when (it) {
                is ContactDetailsState.Idle -> drawIdleState(it)
                is ContactDetailsState.Loading -> drawLoadingState()
                is ContactDetailsState.Error -> drawErrorState()
            }
        })
        with(binding) {
            toolbar.setNavigationOnClickListener {
                viewModel.backPressed()
            }
        }
    }

    private fun drawLoadingState() {
        with(binding) {
            loadingBar.isVisible = true
            scrollView.isVisible = false
        }
    }

    private fun drawIdleState(state: ContactDetailsState.Idle) {
        val contact = state.contact
        with(binding) {
            loadingBar.isVisible = false
            scrollView.isVisible = true

            photo.setImageResource(contact.photo)
            name.text = contact.name
            phone.putText(phoneTitle, contact.phone)
            phone2.putText(phone2Title, contact.phone2)
            email.putText(emailTitle, contact.email)
            email2.putText(email2Title, contact.email2)
            notes.putText(notesTitle, contact.notes)

            if (contact.birthdayTimestamp != 0L) {
                birthdayTitle.isVisible = true
                birthday.isVisible = true
                birthday.text = SimpleDateFormat(BIRTHDAY_DATE_FORMAT, Locale.getDefault())
                    .format(Date(contact.birthdayTimestamp))
                birthdayNotice.isVisible = true
                if (state.birthdayNotice) {
                    birthdayNotice.setImageResource(R.drawable.ic_notifications_active)
                    birthdayNotice.setColorFilter(R.color.teal_500, PorterDuff.Mode.SRC_IN)
                } else {
                    birthdayNotice.setImageResource(R.drawable.ic_notifications_none)
                    birthdayNotice.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
                }
                birthdayNotice.setOnClickListener {
                    viewModel.changeBirthdayNotice()
                }
            } else {
                birthdayTitle.isVisible = false
                birthday.isVisible = false
                birthdayNotice.isVisible = false
            }
        }
    }

    private fun drawErrorState() {
        with(binding) {
            loadingBar.isVisible = false
            scrollView.isVisible = false
        }
    }

    private fun TextView.putText(titleView: View, textValue: String) {
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
