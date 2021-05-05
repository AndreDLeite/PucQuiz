package com.example.pucquiz.ui.quizselector.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.Medals
import com.example.pucquiz.models.Question
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.shared.enums.QuizType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuizConfigurationViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
): AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>>
        get() = _questions

    private var quizType = QuizType.UNKNOWN

    private var quizGrade = GradeEnum.UNKOWN

    fun setQuizType(currentType: QuizType) {
        quizType = currentType
    }

    fun getQuizType() = quizType

    fun setQuizGrade(currentGrade: GradeEnum) {
        quizGrade = currentGrade
    }

    fun getQuizGrad() = quizGrade

    fun fetchGradeQuestions() {
        ioScope.launch {
            _questions.postValue(Resource.loading())
            firebaseRepo.fetchGradeQuestions(
                quizGrade,
                object : FirebaseGradeQuestionsCallBack {
                    override fun onResponse(questions: List<Question>?) {
                        questions?.let {
                            validateQuestionList(it)
                        } ?: run {
                            _questions.postValue(Resource.error("Erro ao buscar perguntas no sistema. Por favor, tente mais tarde.", questions))
                        }
                    }
                })
        }
    }

    private fun validateQuestionList(questionList: List<Question>) {
        if(questionList.size < 5) {
            _questions.postValue(Resource.error("A matéria em questão não possui perguntas suficientes para realização de um Quiz. Por favor, entre em contato com o responsável pela matéria.", null))
        } else {
            _questions.postValue(Resource.success(questionList))
        }
    }

    fun clearViewModel() {
        setQuizType(QuizType.UNKNOWN)
        setQuizGrade(GradeEnum.UNKOWN)
        ioScope.launch {
            _questions.postValue(null)

        }
    }

}