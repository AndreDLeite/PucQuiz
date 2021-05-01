package com.example.pucquiz.ui.quizhall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.models.Question
import com.example.pucquiz.models.User
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuestionsHallViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _teacherQuestions = MutableLiveData<Resource<List<Question>?>>()
    val teacherQuestions: LiveData<Resource<List<Question>?>>
        get() = _teacherQuestions

    fun fetchTeacherQuestions() {
        ioScope.launch {
            FirebaseAuth.getInstance().currentUser?.let {
                firebaseRepo.fetchTeacherQuestions(
                    it.uid,
                    object : FirebaseTeacherQuestionsCallback {
                        override fun onResponse(questions: List<Question>?) {
                            questions?.let {
                                if (questions.isEmpty()) {
                                    _teacherQuestions.postValue(
                                        Resource.error(
                                            "Empty question list",
                                            questions
                                        )
                                    )
                                } else {
                                    _teacherQuestions.postValue(Resource.success(questions))
                                }
                            } ?: run {
                                _teacherQuestions.postValue(Resource.error("questions", questions))
                            }
                        }
                    })
            }
        }
    }
}