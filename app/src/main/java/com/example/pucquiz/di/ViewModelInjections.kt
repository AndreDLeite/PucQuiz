package com.example.pucquiz.di

import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import com.example.pucquiz.ui.medals.viewmodels.MedalsViewModel
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionCreationViewModel
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionsHallViewModel
import com.example.pucquiz.ui.quizprogress.viewmodels.QuizMainViewModel
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
    viewModel { MedalsViewModel(androidApplication(), get()) }
    viewModel { UserInfoViewModel(androidApplication(), get()) }
    viewModel { RankingViewModel(androidApplication(), get()) }
    viewModel { QuestionsHallViewModel(androidApplication(), get()) }
    viewModel { QuestionCreationViewModel(androidApplication(), get()) }
    viewModel { QuizMainViewModel(androidApplication(), get(), get()) }

//    viewModel { WelcomeViewModel(androidApplication()) }
//    viewModel { RedesQuizViewModel(androidApplication()) }
}