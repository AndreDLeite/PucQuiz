package com.example.pucquiz.ui.quizhall.viewmodels

import android.app.Application
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.controllers.QuestionController
import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.Question
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.AppConstants
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class QuestionsHallViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _teacherQuestions = MutableLiveData<Resource<List<Question>?>>()
    val teacherQuestions: LiveData<Resource<List<Question>?>>
        get() = _teacherQuestions

    private val _currentSelectedQuestion = MutableLiveData<Question>()
    val currentSelectedQuestion: LiveData<Question>
        get() = _currentSelectedQuestion

    private val _updateOperationStatus = MutableLiveData<Resource<Boolean>>()
    val updateOperationStatus: LiveData<Resource<Boolean>>
        get() = _updateOperationStatus

    private val _deleteOperationStatus = MutableLiveData<Resource<Boolean>>()
    val deleteOperationStatus: LiveData<Resource<Boolean>>
        get() = _deleteOperationStatus

    private var registerSuccess: Boolean = false
    var questionSummary = ""
    var answersSummary = HashMap<String, Boolean>()

    var answerMap = HashMap<EditText, Answer>()

    fun fetchTeacherQuestions() {
        ioScope.launch {
            delay(1000)
            _teacherQuestions.postValue(Resource.loading())
            FirebaseAuth.getInstance().currentUser?.let {
                firebaseRepo.fetchTeacherQuestions(
                    it.uid,
                    object : FirebaseTeacherQuestionsCallback {
                        override fun onResponse(questions: List<Question>?) {
                            questions?.let {
                                _teacherQuestions.postValue(Resource.success(questions))
                            } ?: run {
                                _teacherQuestions.postValue(
                                    Resource.error(
                                        "error fetch questions",
                                        questions
                                    )
                                )
                            }
                        }
                    })
            } ?: run {
                _teacherQuestions.postValue(Resource.error("error fetch questions", null))
            }
        }
    }

    fun updateQuestion() {
        ioScope.launch {
            _updateOperationStatus.postValue(Resource.loading())
            currentSelectedQuestion.value?.let { currentQuestion ->
                FirebaseAuth.getInstance().currentUser?.let { itCurrentUser ->
                    val newAnswerList = answerMap.map { it.value }
                    val question = QuestionController().generateUpdatedQuestion(
                        teacherId = itCurrentUser.uid,
                        summary = questionSummary,
                        answerList = newAnswerList,
                        newQuestionData = currentQuestion
                    )
                    val questionOperation = sendQuestionToFirebase(question)
                    _updateOperationStatus.postValue(
                        Resource.success(questionOperation)
                    )
                }
            }
        }
    }

    private fun sendQuestionToFirebase(question: Question): Boolean {
        var operationResult = false
        FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_TEACHER_QUESTIONS_BUCKET)
            .child(question.id)
            .setValue(question)
            .addOnCompleteListener { itRTDBTask ->
                operationResult = when {
                    itRTDBTask.isSuccessful -> {
                        operationResult = true
                        Log.e("Question Update", "Updated with success!")
                        true
                    }

                    else -> {
                        operationResult = false
                        Log.e("Question Update", "Failed to update...")
                        false
                    }
                }
            }
        return operationResult
    }

    fun deleteCurrentQuestion() {
        ioScope.launch {
            _deleteOperationStatus.postValue(Resource.loading())
            currentSelectedQuestion.value?.let { currentQuestion ->
                val questionOperation = deleteQuestionFromFirebase(currentQuestion)
                _deleteOperationStatus.postValue(
                    Resource.success(questionOperation)
                )
            }
        }
    }

    private fun deleteQuestionFromFirebase(currentQuestion: Question): Boolean {
        var operationResult = false
        FirebaseDatabase.getInstance().getReference(AppConstants.FIREBASE_TEACHER_QUESTIONS_BUCKET)
            .child(currentQuestion.id)
            .removeValue()
            .addOnCompleteListener { itRTDBTask ->
                operationResult = when {
                    itRTDBTask.isSuccessful -> {
                        Log.e("Question Deletion", "Deleted with success!")
                        true
                    }

                    else -> {
                        Log.e("Question Deletion", "Failed to delete...")
                        false
                    }
                }
            }
        return operationResult
    }

    fun setRegistrationValue(value: Boolean) {
        registerSuccess = value
    }

    fun getRegistrationValue() = registerSuccess

    fun serCurrentSelectedQuestion(newValue: Question) {
        ioScope.launch {
            _currentSelectedQuestion.postValue(newValue)
        }
    }

    fun clearCurrentSelectedQuestion() {
        ioScope.launch {
            _currentSelectedQuestion.postValue(null)
        }
    }

    fun clearViewModel() {
        registerSuccess = false
        questionSummary = ""
        answersSummary = hashMapOf()
        ioScope.launch {
            _currentSelectedQuestion.postValue(null)
            _updateOperationStatus.postValue(null)
            _deleteOperationStatus.postValue(null)
        }
    }
}