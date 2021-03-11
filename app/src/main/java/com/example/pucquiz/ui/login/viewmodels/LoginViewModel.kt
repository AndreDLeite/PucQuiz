package com.example.pucquiz.ui.login.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class LoginViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var registerSuccess: Boolean = false

    private val _loginLiveData = MutableLiveData<Resource<Boolean>>()
    val loginLiveData: LiveData<Resource<Boolean>>
        get() = _loginLiveData


    fun loginUser(userEmail: String, userPassword: String) {
        ioScope.launch {
            _loginLiveData.postValue(Resource.loading())
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { itTask ->
                    if(itTask.isSuccessful) {
                        _loginLiveData.postValue(Resource.success(true))
                    } else {
                        _loginLiveData.postValue(Resource.error("Error validating user data from server", false))
                    }
                }
        }
    }

    fun setRegistrationValue(value: Boolean) {
        registerSuccess = value
    }

    fun getRegistrationValue() = registerSuccess

}