package com.example.pucquiz.ui.quizhall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.controllers.QuestionController
import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.AnswerAdditionalInfo
import com.example.pucquiz.models.Question
import com.example.pucquiz.models.QuestionAdditionalInfo
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.AppConstants
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.shared.enums.QuizType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

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

    private var currentQuestionType = QuizType.TEACHER

    fun createQuestion() {
        ioScope.launch {
            _questionRegistrationLiveData.postValue(Resource.loading())
            val gradeEnum = GradeController().gradeFromString(currentQuestionGrade)
            FirebaseAuth.getInstance().currentUser?.let { itCurrentUser ->
                val question = QuestionController().generateQuestion(
                    teacherId = itCurrentUser.uid,
                    summary = questionSummary,
                    answerList = answers,
                    questionGrade = gradeEnum,
                    questionType = currentQuestionType

                )
                val questionOperation = sendQuestionToFirebase(question)
                val questionAddInfoOperation = sendQuestionAddInfoToFirebase(question)
                _questionRegistrationLiveData.postValue(
                    Resource.success(questionOperation && questionAddInfoOperation)
                )
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

    private fun sendQuestionAddInfoToFirebase(question: Question): Boolean {
        var operationResult = false
        ioScope.launch {
            val questionAdditionalInfo = generateQuestionAdditionalInfo(question)
            operationResult =
                firebaseRepo.sendQuestionAddInfoToFirebase(question.id, questionAdditionalInfo)
        }

        return operationResult
    }

    private fun generateQuestionAdditionalInfo(question: Question): QuestionAdditionalInfo {
        val answerAdditionalInfo = generateAnswersAdditionalInfo(question.answers)
        return QuestionAdditionalInfo(
            questionId = question.id,
            timesAnswered = 0,
            answersAdditionalInfo = answerAdditionalInfo
        )
    }

    private fun generateAnswersAdditionalInfo(answers: List<Answer>): List<AnswerAdditionalInfo> {
        val answerList = mutableListOf<AnswerAdditionalInfo>()
        answers.forEach {
            answerList.add(
                AnswerAdditionalInfo(
                    answerId = it.id,
                    correctAnswer = it.correctAnswer,
                    timesAnswered = 0
                )
            )
        }
        return answerList
    }

    fun setCurrentQuestionType(newValue: QuizType) {
        currentQuestionType = newValue
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