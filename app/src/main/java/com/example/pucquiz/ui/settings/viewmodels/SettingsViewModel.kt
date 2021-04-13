package com.example.pucquiz.ui.settings.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    fun logOutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
    }
}