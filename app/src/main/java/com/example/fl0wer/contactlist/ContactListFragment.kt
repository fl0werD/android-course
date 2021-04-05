package com.example.fl0wer.contactlist

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fl0wer.R
import com.example.fl0wer.databinding.FragmentContactListBinding
import com.example.fl0wer.repository.ContactsRepository

class ContactListFragment : Fragment(
    R.layout.fragment_contact_list
) {
    private val binding: FragmentContactListBinding by viewBinding()
    private val viewModelFactory by lazy {
        ContactListViewModelFactory(
            ContactsRepository.get(requireContext()),
        )
    }
    private val viewModel: ContactListViewModel by viewModels {
        viewModelFactory
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getScreenState().observe(viewLifecycleOwner, {
            when (it) {
                is ContactListState.Idle -> drawIdleState(it)
                is ContactListState.Loading -> drawLoadingState()
                is ContactListState.Empty -> drawEmptyState()
            }
        })
    }

    private fun drawEmptyState() {
        with(binding) {
            loadingBar.isVisible = false
            scrollView.isVisible = true
        }
    }

    private fun drawLoadingState() {
        with(binding) {
            loadingBar.isVisible = true
            scrollView.isVisible = false
        }
    }

    private fun drawIdleState(state: ContactListState.Idle) {
        val contact = state.contact
        with(binding) {
            loadingBar.isVisible = false
            scrollView.isVisible = true
            with(contactCard) {
                root.isVisible = true
                root.setOnClickListener {
                    viewModel.openContact(contact)
                }
                photo.setImageResource(contact.photo)
                name.text = contact.name
                phoneNumber.text = contact.phone
            }
        }
    }
}
