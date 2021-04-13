package com.example.pucquiz.di

import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoVewModel
import com.example.pucquiz.ui.ranking.viewmodels.RankingViewModel
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import com.example.pucquiz.ui.settings.viewmodels.SettingsViewModel
import com.example.pucquiz.ui.splash.SplashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

fun Module.viewModelInjections() {
    viewModel { SplashViewModel(androidApplication()) }
    viewModel { LoginViewModel(androidApplication()) }
    viewModel { RegisterViewModel(androidApplication()) }
    viewModel { SettingsViewModel(androidApplication()) }
    viewModel { UserInfoVewModel(androidApplication()) }
    viewModel { RankingViewModel(androidApplication(), get()) }

//    viewModel { WelcomeViewModel(androidApplication()) }
//    viewModel { RedesQuizViewModel(androidApplication()) }
}