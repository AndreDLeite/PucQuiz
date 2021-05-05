package com.example.pucquiz.ui.quizhall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.controllers.QuestionController
import com.example.pucquiz.models.Question
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.AppConstants
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class QuestionCreationViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _questionRegistrationLiveData = MutableLiveData<Resource<Boolean>>()
    val questionRegistrationLiveData: LiveData<Resource<Boolean>>
        get() = _questionRegistrationLiveData

    var currentQuestionGrade = ""
    var questionSummary = ""
    var answers = HashMap<String, Boolean>()

    fun createQuestion() {
        ioScope.launch {
            _questionRegistrationLiveData.postValue(Resource.loading())
            val gradeEnum = GradeController().gradeFromString(currentQuestionGrade)
            FirebaseAuth.getInstance().currentUser?.let { itCurrentUser ->
                val question = QuestionController().generateQuestion(
                    teacherId = itCurrentUser.uid,
                    summary = questionSummary,
                    answerList = answers,
                    questionGrade = gradeEnum
                )
                _questionRegistrationLiveData.postValue(
                    Resource.success(
                        sendQuestionToFirebase(
                            question
                        )
                    )
                )
            }
        }
    }

    private fun sendQuestionToFirebase(question: Question): Boolean {
        var operationResult = false
        val randomId = UUID.randomUUID().toString()
        FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_TEACHER_QUESTIONS_BUCKET)
            .child(randomId)
            .setValue(question)
            .addOnCompleteListener { itRTDBTask ->
                operationResult = when {
                    itRTDBTask.isSuccessful -> {
                        Log.e("Question Creation", "Created with success!")
                        true
                    }

                    else -> {
                        Log.e("Question Creation", "Failed to create...")
                        false
                    }
                }
            }
        return operationResult
    }

    fun clearViewModel() {
        currentQuestionGrade = ""
        questionSummary = ""
        answers = hashMapOf()
        ioScope.launch {
            _questionRegistrationLiveData.postValue(null)
        }
    }

}