package com.example.fl0wer.androidApp.ui.core

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fl0wer.androidApp.di.core.ViewModelFactory
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.util.Const.BUNDLE_INITIAL_ARGS
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

abstract class BaseFragment<VM : BaseViewModel<S>, S : UiState> : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected abstract val vmClass: Class<VM>
    protected val viewModel: VM by lazy {
        ViewModelProvider(this, viewModelFactory).get(vmClass)
    }

    protected open fun updateState(state: S) {
        // for implementing
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.uiState().collect {
                updateState(it)
            }
        }
    }

    fun <A : Parcelable> initialArguments(): A {
        arguments?.getParcelable<A>(BUNDLE_INITIAL_ARGS)?.also { return it }
        throw IllegalArgumentException("Fragment doesn't contain initial args")
    }
}
