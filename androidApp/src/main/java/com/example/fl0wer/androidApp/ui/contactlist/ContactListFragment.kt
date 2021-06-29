package com.example.fl0wer.androidApp.ui.contactlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.di.core.ViewModelFactory
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactsAdapter
import com.example.fl0wer.androidApp.ui.contactlist.adapter.contactItemDecorator
import com.example.fl0wer.databinding.FragmentContactListBinding
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ContactListFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ContactListViewModel by viewModels { viewModelFactory }
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
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateState(it)
            }
        }
    }

    private fun updateState(state: UiState) {
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
