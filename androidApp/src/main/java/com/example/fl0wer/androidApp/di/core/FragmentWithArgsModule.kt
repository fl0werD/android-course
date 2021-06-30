package com.example.fl0wer.androidApp.di.core

import android.os.Parcelable
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.androidApp.ui.core.BaseFragment
import dagger.Module
import dagger.Provides

@Module
abstract class FragmentWithArgsModule<F : BaseFragment<*, S>, S : UiState, A : Parcelable> {
    @Provides
    fun provideInitialArgs(fragment: F): A = fragment.initialArguments()
}
