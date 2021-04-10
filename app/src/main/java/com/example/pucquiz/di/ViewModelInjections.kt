package com.example.pucquiz.di

import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.mainboard.viewmodels.OnBoardingViewModel
import com.example.pucquiz.ui.mainboard.viewmodels.RankingViewModel
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

fun Module.viewModelInjections() {
    viewModel { LoginViewModel(androidApplication()) }
    viewModel { RegisterViewModel(androidApplication()) }
    viewModel { OnBoardingViewModel(androidApplication()) }
    viewModel { RankingViewModel(androidApplication()) }

//    viewModel { WelcomeViewModel(androidApplication()) }
//    viewModel { RedesQuizViewModel(androidApplication()) }
}