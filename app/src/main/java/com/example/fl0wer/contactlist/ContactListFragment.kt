package com.example.fl0wer.contactlist

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val contactsAdapter by lazy {
        ContactsAdapter(
            { binding.contactsList.layoutManager?.scrollToPosition(0) },
            { position -> viewModel.contactClicked(position) }
        )
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.contactsList) {
            layoutManager = LinearLayoutManager(context).apply { recycleChildrenOnDetach = true }
            adapter = contactsAdapter
            addItemDecoration(contactItemDecorator(context))
        }
        with(view.findViewById<SearchView>(R.id.app_bar_search)) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchTextChanged(newText)
                    return true
                }
            })
        }
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
            contactsList.isVisible = true
        }
    }

    private fun drawLoadingState() {
        with(binding) {
            loadingBar.isVisible = true
            contactsList.isVisible = false
        }
    }

    private fun drawIdleState(state: ContactListState.Idle) {
        contactsAdapter.items = state.contacts
        with(binding) {
            loadingBar.isVisible = false
            contactsList.isVisible = true
        }
    }
}
