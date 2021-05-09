package com.example.pucquiz.di

import com.example.pucquiz.controllers.QuizController
import org.koin.core.module.Module


fun Module.controllersInjection() {
    factory { QuizController(get()) }
}