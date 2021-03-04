package com.example.fl0wer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class ContactListFragment : Fragment() {
    private val firstContact = Contacts.contacts.firstOrNull()

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firstContact != null) {
            val contactCard = view.findViewById<CardView>(R.id.contact)
            contactCard.fillContact(firstContact)
            contactCard.setOnClickListener {
                contactClicked()
            }
        }
    }

    private fun contactClicked() {
        val fm = fragmentManager ?: return
        val contact = firstContact ?: return
        val contactDetailsFragment = ContactDetailsFragment.newInstance(contact.id)
        fm.beginTransaction()
            .replace(R.id.fragments_container, contactDetailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun CardView.fillContact(contact: Contact) {
        findViewById<ImageView>(R.id.photo).setImageResource(contact.photo)
        findViewById<TextView>(R.id.name).text = contact.name
        findViewById<TextView>(R.id.phoneNumber).text = contact.phone
    }
}
