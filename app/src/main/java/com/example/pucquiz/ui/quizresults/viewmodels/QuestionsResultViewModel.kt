package com.example.pucquiz.ui.quizresults.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.Question
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.shared.enums.QuizType
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuestionsResultViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private var currentTeacherGrade = GradeEnum.UNKOWN
    private var currentQuestion: Question? = null

    private val _teacherQuestions = MutableLiveData<Resource<List<Question>?>>()
    val teacherQuestions: LiveData<Resource<List<Question>?>>
        get() = _teacherQuestions

    fun setCurrentTeacherGrade(grade: GradeEnum) {
        currentTeacherGrade = grade
    }

    fun setCurrentQuestion(question: Question) {
        currentQuestion = question
    }

    fun clearCurrentQuestion() {
        currentQuestion = null
    }

    fun clearViewModel() {
        currentTeacherGrade = GradeEnum.UNKOWN
        currentQuestion = null
        ioScope.launch {
            _teacherQuestions.postValue(null)
        }
    }

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
                                val filteredQuestions = questions.filter { it.questionGrade == currentTeacherGrade }
                                _teacherQuestions.postValue(Resource.success(filteredQuestions))
                            } ?: run {
                                _teacherQuestions.postValue(Resource.error("error fetch questions", questions))
                            }
                        }
                    })
            } ?: run {
                _teacherQuestions.postValue(Resource.error("error fetch questions", null))
            }
        }
    }

}