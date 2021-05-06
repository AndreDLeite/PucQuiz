package com.example.pucquiz.ui.quizselector.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.models.Answer
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
) : AndroidViewModel(application) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>>
        get() = _questions

    private val _currentQuestion = MutableLiveData<Resource<Question>>()
    val currentQuestion: LiveData<Resource<Question>>
        get() = _currentQuestion

    private val _isQuizOver = MutableLiveData<Boolean>()
    val isQuizOver: LiveData<Boolean>
        get() = _isQuizOver

    private var quizType = QuizType.UNKNOWN

    private var quizGrade = GradeEnum.UNKOWN

    private var currentQuestions = mutableListOf<Question>()

    private var answeredQuestions = hashMapOf<Question, Answer>()

    private var count = 0

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
                            _questions.postValue(
                                Resource.error(
                                    "Erro ao buscar perguntas no sistema. Por favor, tente mais tarde.",
                                    questions
                                )
                            )
                        }
                    }
                })
        }
    }

    private fun validateQuestionList(questionList: List<Question>) {
        if (questionList.size < 5) {
            _questions.postValue(
                Resource.error(
                    "A matéria em questão não possui perguntas suficientes para realização de um Quiz. Por favor, entre em contato com o responsável pela matéria.",
                    null
                )
            )
        } else {
            _questions.postValue(Resource.success(questionList))
        }
    }

    fun clearViewModel() {
        setQuizType(QuizType.UNKNOWN)
        setQuizGrade(GradeEnum.UNKOWN)
        ioScope.launch {
            _questions.postValue(null)
            _currentQuestion.postValue(null)
            _isQuizOver.postValue(null)
        }
    }

    fun shuffleQuiz() {
        _questions.value?.data?.let { itQuestionList ->
            itQuestionList.forEach {
                it.answers = it.answers.shuffled()
            }
            currentQuestions.addAll(itQuestionList.shuffled())
        }
    }

    fun getNextQuestion() {
        ioScope.launch {
            _currentQuestion.postValue(Resource.loading())
            count++
            if(!currentQuestions.isNullOrEmpty()) {
                val randomQuestion = currentQuestions.random()
                currentQuestions.remove(randomQuestion)
                _currentQuestion.postValue(Resource.success(randomQuestion))
            } else {
                _isQuizOver.postValue(true)
            }
        }
    }

    fun addAnswerToQuestion(answer: Answer?) {
        _currentQuestion.value?.data?.let { itQuestion ->
            answer?.let { itAnswer ->
                answeredQuestions[itQuestion] = itAnswer
            }
        }
    }

    fun getCurrentCount() = count
}