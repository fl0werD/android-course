package com.example.fl0wer.androidApp.ui.contactlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactsAdapter
import com.example.fl0wer.androidApp.ui.contactlist.adapter.contactItemDecorator
import com.example.fl0wer.androidApp.ui.core.BaseFragment
import com.example.fl0wer.databinding.FragmentContactListBinding

class ContactListFragment : BaseFragment<ContactListViewModel, ContactListState>() {
    override val vmClass = ContactListViewModel::class.java
    private lateinit var binding: FragmentContactListBinding
    private val contactsAdapter by lazy {
        ContactsAdapter { position ->
            viewModel.contactClicked(position)
        }
    }

    companion object {
        fun newInstance() = ContactListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentContactListBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.toolbar.menu.findItem(R.id.menu_search).actionView as SearchView) {
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchTextChanged(newText.trim())
                    return true
                }
            })
        }
        with(binding.contactsList) {
            layoutManager = LinearLayoutManager(context).apply { recycleChildrenOnDetach = true }
            adapter = contactsAdapter
            addItemDecoration(contactItemDecorator(context))
        }
        with(binding) {
            swipeToRefresh.setOnRefreshListener {
                viewModel.swipeRefresh()
            }
            showAllPins.setOnClickListener {
                viewModel.contactPinsClicked()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadContacts()
        }
    }

    override fun updateState(state: ContactListState) {
        when (state) {
            is ContactListState.Idle -> drawIdleState(state)
            is ContactListState.Loading -> drawLoadingState()
            is ContactListState.Failure -> drawFailureState()
        }
    }

    private fun drawIdleState(state: ContactListState.Idle) = with(binding) {
        contactsAdapter.items = state.contacts
        loadingBar.isVisible = false
        swipeToRefresh.isRefreshing = false
        contactsList.isVisible = true
        showAllPins.isVisible = true
    }

    private fun drawLoadingState() = with(binding) {
        loadingBar.isVisible = true
        swipeToRefresh.isRefreshing = false
        contactsList.isVisible = false
        showAllPins.isVisible = false
    }

    private fun drawFailureState() = with(binding) {
        loadingBar.isVisible = false
        swipeToRefresh.isRefreshing = false
        contactsList.isVisible = true
        showAllPins.isVisible = false
    }
}
