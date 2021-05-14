package com.example.pucquiz.ui.forgotpassword.viewmodels

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

class ForgotPasswordViewModel(application: Application): AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _operationStatus = MutableLiveData<Resource<Boolean>>()
    val operationStatus: LiveData<Resource<Boolean>>
        get() = _operationStatus

    fun sendResetPassword(userMail: String) {
        ioScope.launch {
            _operationStatus.postValue(Resource.loading())
            auth.sendPasswordResetEmail(userMail)
                .addOnCompleteListener { itTask ->
                    if(itTask.isSuccessful) {
                        _operationStatus.postValue(Resource.success(true))
                    } else {
                        _operationStatus.postValue(Resource.error("Erro ao enviar email de recuperação. Por favor, tente novamente mais tarde.", false))
                    }
                }
        }
    }

    fun clearViewModel() {
        ioScope.launch {
            _operationStatus.postValue(null)
        }
    }

}