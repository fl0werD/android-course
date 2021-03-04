package com.example.fl0wer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class ContactDetailsFragment : Fragment() {
    companion object {
        private const val ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID"

        fun newInstance(
            contactId: Int,
        ): ContactDetailsFragment = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARGUMENT_CONTACT_ID to contactId,
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
        toolBar.setNavigationOnClickListener {
            backPressed()
        }
        arguments?.let {
            val contactId = it.getInt(ARGUMENT_CONTACT_ID, 0)
            val contact = Contacts.getUserById(contactId) ?: return
            view.findViewById<ImageView>(R.id.photo).setImageResource(contact.photo)
            view.findViewById<TextView>(R.id.name).text = contact.name
            view.findViewById<TextView>(R.id.phone).text = contact.phone
            view.findViewById<TextView>(R.id.phone2).text = contact.phone2
            view.findViewById<TextView>(R.id.email).text = contact.email
            view.findViewById<TextView>(R.id.email2).text = contact.email2
            view.findViewById<TextView>(R.id.notes).text = contact.notes
        }
    }

    private fun backPressed() {
        fragmentManager?.popBackStack()
    }
}
