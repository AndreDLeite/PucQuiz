package com.example.pucquiz.di

import com.example.pucquiz.ui.welcome.viewmodels.WelcomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module


fun Module.viewModelInjections() {
    viewModel { WelcomeViewModel(androidApplication()) }
}