package com.example.fl0wer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment(
    R.layout.fragment_contact_list
) {
    private var uiState = ContactListState.EMPTY
    private var firstContact: Contact? = null
    private val mainHandler: Handler = Handler(Looper.getMainLooper())
    private val contactServiceListener = ConnectionListener {
        fetchFirstContact()
    }

    private lateinit var contactService: IContactServiceListener
    private lateinit var contactClickListener: ContactClickListener

    private lateinit var loadingBar: ProgressBar
    private lateinit var contactCard: CardView
    private lateinit var contactPhoto: ImageView
    private lateinit var contactName: TextView
    private lateinit var contactPhone: TextView

    companion object {
        enum class ContactListState {
            EMPTY,
            LOADING,
            IDLE,
        }

        fun newInstance() = ContactListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IContactServiceListener) {
            contactService = context
        } else {
            throw IllegalStateException("Context is not implement contact service interface")
        }
        if (context is ContactClickListener) {
            contactClickListener = context
        } else {
            throw IllegalStateException("Context is not implement contact click listener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingBar = view.findViewById(R.id.loading_bar)
        contactCard = view.findViewById(R.id.contact)
        contactPhoto = view.findViewById(R.id.photo)
        contactName = view.findViewById(R.id.name)
        contactPhone = view.findViewById(R.id.phoneNumber)

        updateState(ContactListState.EMPTY)
        contactService.subscribeContactService(contactServiceListener)
        fetchFirstContact()
    }

    override fun onDestroyView() {
        mainHandler.removeCallbacksAndMessages(null)
        contactService.unsubscribeContactService(contactServiceListener)
        super.onDestroyView()
    }

    private fun fetchFirstContact() {
        updateState(ContactListState.LOADING)
        contactService.contactService()?.getFirstContact {
            mainHandler.post {
                showResult(it)
            }
        }
    }

    private fun showResult(contact: Contact?) {
        firstContact = contact
        updateState(ContactListState.IDLE)
    }

    private fun updateState(state: ContactListState) {
        uiState = state
        when (state) {
            ContactListState.EMPTY -> {
                loadingBar.isVisible = false
                contactCard.isVisible = false
            }
            ContactListState.LOADING -> {
                loadingBar.isVisible = true
                contactCard.isVisible = false
            }
            ContactListState.IDLE -> {
                loadingBar.isVisible = false
                drawContact(firstContact)
            }
        }
    }

    private fun drawContact(contact: Contact?) {
        with(contactCard) {
            if (contact != null) {
                isVisible = true
                contactPhoto.setImageResource(contact.photo)
                contactName.text = contact.name
                contactPhone.text = contact.phone
                setOnClickListener {
                    contactClicked(contact.id)
                }
            } else {
                isVisible = false
                setOnClickListener(null)
            }
        }
    }

    private fun contactClicked(id: Int) {
        contactClickListener.openContact(id)
    }
}
