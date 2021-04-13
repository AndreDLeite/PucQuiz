package com.example.pucquiz.di

import com.example.pucquiz.repositories.firebasertdb.FirebaseRTDBRepository
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import org.koin.core.module.Module

fun Module.repositoryInjections() {
    single { FirebaseRTDBRepository() as IFirebaseRTDBRepository }
}
