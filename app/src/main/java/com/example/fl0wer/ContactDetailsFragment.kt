package com.example.fl0wer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment(
    R.layout.fragment_contact_details
) {
    private var uiState = ContactDetailsState.EMPTY
    private var detailedContact: Contact? = null
    private val mainHandler: Handler = Handler(Looper.getMainLooper())
    private val contactServiceListener = ConnectionListener {
        fetchContact()
    }

    private lateinit var contactService: IContactServiceListener

    private lateinit var toolbar: Toolbar
    private lateinit var loadingBar: ProgressBar
    private lateinit var detailsLayout: ScrollView
    private lateinit var contactPhoto: ImageView
    private lateinit var contactName: TextView
    private lateinit var contactPhone: TextView
    private lateinit var contactPhone2: TextView
    private lateinit var contactEmail: TextView
    private lateinit var contactEmail2: TextView
    private lateinit var contactNotes: TextView

    companion object {
        enum class ContactDetailsState {
            EMPTY,
            LOADING,
            IDLE,
        }

        private const val ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID"
        private const val UNDEFINED_CONTACT_ID = -1

        fun newInstance(
            contactId: Int,
        ): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARGUMENT_CONTACT_ID to contactId,
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IContactServiceListener) {
            contactService = context
        } else {
            throw IllegalStateException("Context is not implement contact service interface")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        loadingBar = view.findViewById(R.id.loading_bar)
        detailsLayout = view.findViewById(R.id.details_layout)
        contactPhoto = view.findViewById(R.id.photo)
        contactName = view.findViewById(R.id.name)
        contactPhone = view.findViewById(R.id.phone)
        contactPhone2 = view.findViewById(R.id.phone2)
        contactEmail = view.findViewById(R.id.email)
        contactEmail2 = view.findViewById(R.id.email2)
        contactNotes = view.findViewById(R.id.notes)

        toolbar.setNavigationOnClickListener {
            backPressed()
        }
        updateState(ContactDetailsState.EMPTY)
        contactService.subscribeContactService(contactServiceListener)
        fetchContact()
    }

    override fun onDestroyView() {
        mainHandler.removeCallbacksAndMessages(null)
        contactService.unsubscribeContactService(contactServiceListener)
        super.onDestroyView()
    }

    private fun fetchContact() {
        val contactId = requireArguments().getInt(ARGUMENT_CONTACT_ID, UNDEFINED_CONTACT_ID)

        require(contactId != UNDEFINED_CONTACT_ID) {
            "Contact id argument required"
        }

        updateState(ContactDetailsState.LOADING)
        contactService.contactService()?.getContactById(contactId) {
            mainHandler.post {
                showResult(it)
            }
        }
    }

    private fun showResult(contact: Contact?) {
        detailedContact = contact
        updateState(ContactDetailsState.IDLE)
    }

    private fun updateState(state: ContactDetailsState) {
        uiState = state
        when (state) {
            ContactDetailsState.EMPTY -> {
                loadingBar.isVisible = false
                detailsLayout.isVisible = false
            }
            ContactDetailsState.LOADING -> {
                loadingBar.isVisible = true
                detailsLayout.isVisible = false
            }
            ContactDetailsState.IDLE -> {
                loadingBar.isVisible = false
                detailsLayout.isVisible = true
                drawContactDetails(detailedContact)
            }
        }
    }

    private fun drawContactDetails(contact: Contact?) {
        if (contact == null) {
            return
        }
        contactPhoto.setImageResource(contact.photo)
        contactName.text = contact.name
        contactPhone.text = contact.phone
        contactPhone2.text = contact.phone2
        contactEmail.text = contact.email
        contactEmail2.text = contact.email2
        contactNotes.text = contact.notes
    }

    private fun backPressed() {
        fragmentManager?.popBackStack()
    }
}
