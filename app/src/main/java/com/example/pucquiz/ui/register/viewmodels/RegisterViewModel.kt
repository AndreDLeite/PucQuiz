package com.example.pucquiz.ui.register.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.controllers.UserAdditionalInfoController
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserRole
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_INFO_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_MEDALS_BUCKET
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

    var currentUserRole = UserRole.STUDENT

    init {
        _selectedGrades.value = mutableListOf()
        _userPeriod.value = -1
    }

    fun resetRegistrationOperation() {
        _registrationLiveData.value = null
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
        when(currentUserRole) {
            UserRole.STUDENT -> {
                _generatedGrades.value = GradeController().generateBasedOnPeriodGrades(userPeriod)
            }
            UserRole.TEACHER -> {
                _generatedGrades.value = GradeController().generateBasedOnPeriodGrades(5)
            }
        }

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
                            period = userPeriod.value ?: 1,
                            registered_grades = selectedGrades.value ?: emptyList(),
                            role = currentUserRole
                        )
                        createUserInstance(user)
                        if(currentUserRole == UserRole.STUDENT) {
                            createUserAdditionalInfoInstance(user)
                            createUserMedalsInstance(user)
                        }
                        _registrationLiveData.postValue(Resource.success(true))

                    } else {
                        _registrationLiveData.postValue(
                            Resource.error(
                                "Error registering user",
                                null
                            )
                        )
                        Log.d("task resonse", "${itTask.exception}")
                    }
                }
        }

    }

    private fun createUserInstance(user: User): Boolean {
        var operationResult = false
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            FirebaseDatabase.getInstance().getReference(FIREBASE_USER_BUCKET)
                .child(userId)
                .setValue(user)
                .addOnCompleteListener { itRTDBTask ->
                    operationResult = when {
                        itRTDBTask.isSuccessful -> {
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
        }
        return operationResult
    }

    private fun createUserAdditionalInfoInstance(user: User): Boolean {
        var operationResult = false
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            val currentUserAdditionalInfo =
                UserAdditionalInfoController().createUserAdditionalInfo(user)
            FirebaseDatabase.getInstance().getReference(FIREBASE_USER_INFO_BUCKET)
                .child(userId)
                .setValue(currentUserAdditionalInfo)
                .addOnCompleteListener { itRTDBTask ->
                    operationResult = when {
                        itRTDBTask.isSuccessful -> {
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
        }
        return operationResult

    }

    private fun createUserMedalsInstance(user: User): Boolean {
        var operationResult = false
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            val currentUserAdditionalInfo =
                UserAdditionalInfoController().createUserMedals(user)
            FirebaseDatabase.getInstance().getReference(FIREBASE_USER_MEDALS_BUCKET)
                .child(userId)
                .setValue(currentUserAdditionalInfo)
                .addOnCompleteListener { itRTDBTask ->
                    operationResult = when {
                        itRTDBTask.isSuccessful -> {
                            true
                        }

                        else -> {
                            false
                        }
                    }
                }
        }
        return operationResult
    }

    fun clearViewModel() {
        _registrationLiveData.postValue(null)
        _selectedGrades.postValue(null)
        _generatedGrades.postValue(null)
        _userPeriod.postValue(null)
        currentUserRole = UserRole.STUDENT
    }

}