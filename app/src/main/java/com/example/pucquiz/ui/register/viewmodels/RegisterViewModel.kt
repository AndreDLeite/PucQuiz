package com.example.pucquiz.ui.register.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.models.QuestionItem
import com.example.pucquiz.models.User
import com.example.pucquiz.shared.Resource
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class RegisterViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registrationLiveData = MutableLiveData<Resource<Boolean>>()
    val registrationLiveData: LiveData<Resource<Boolean>>
        get() = _registrationLiveData

    fun registerUser(userName: String, userAge: Int, userEmail: String, userPassword: String) {
        ioScope.launch {
            _registrationLiveData.postValue(Resource.loading(null))
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { itTask ->
                    if (itTask.isSuccessful) {
                        val user = User(
                            name = userName,
                            age = userAge,
                            email = userEmail
                        )
                        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(userId)
                                .setValue(user)
                                .addOnCompleteListener { itRTDBTask ->
                                    when {
                                        itRTDBTask.isSuccessful -> {
                                            _registrationLiveData.postValue(Resource.success(true))
                                        }

                                        else -> {
                                            _registrationLiveData.postValue(Resource.error("Error registering user", null))
                                        }
                                    }
                                }
                        }
                    }
                }
        }

    }

}