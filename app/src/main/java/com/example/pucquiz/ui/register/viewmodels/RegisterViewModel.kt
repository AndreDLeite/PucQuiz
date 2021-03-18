package com.example.pucquiz.ui.register.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.User
import com.example.pucquiz.shared.Resource
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

    private val _selectedGrades = MutableLiveData<List<Grade>>()
    val selectedGrades: LiveData<List<Grade>>
        get() = _selectedGrades

    private val _generatedGrades = MutableLiveData<List<Grade>>()
    val generatedGrades: LiveData<List<Grade>>
        get() = _generatedGrades

    private val _userPeriod = MutableLiveData<Int>()
    val userPeriod: LiveData<Int>
        get() = _userPeriod

    var isRegularUser = false

    init {
        _selectedGrades.value = mutableListOf()
        _userPeriod.value = -1
    }

    fun setSelectedGrades(selectedGrades: List<Grade>) {
        _selectedGrades.value = selectedGrades
    }

    fun resetSelectedList() {
        _selectedGrades.value = emptyList()
    }

    fun setUserPeriod(userPeriod: Int) {
        _userPeriod.value = userPeriod
    }

    fun generateGradesBasedOnPeriod(userPeriod: Int) {
        _generatedGrades.value = GradeController().generateBasedOnPeriodGrades(userPeriod)
    }

    fun registerUser(userName: String, userAge: Int, userEmail: String, userPassword: String) {
        ioScope.launch {
            _registrationLiveData.postValue(Resource.loading())
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { itTask ->
                    if (itTask.isSuccessful) {
                        val user = User(
                            name = userName,
                            age = userAge,
                            email = userEmail,
                            registeredGrades = generatedGrades.value ?: emptyList()
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
                    } else {
                        _registrationLiveData.postValue(Resource.error("Error registering user", null))
                        Log.d("task resonse", "${itTask.exception}")
                    }
                }
        }

    }

}