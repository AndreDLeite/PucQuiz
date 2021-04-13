package com.example.pucquiz.di

import org.koin.dsl.module

class AppModules {

    fun createModules() = module {
        viewModelInjections()
        repositoryInjections()
    }

}