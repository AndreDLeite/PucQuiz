package com.example.pucquiz.ui.quizresults.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.callbacks.SingleQuestionAddInfoCallback
import com.example.pucquiz.models.AnswerAdditionalInfo
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.Question
import com.example.pucquiz.models.QuestionAdditionalInfo
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

    private val _questionAdditionalInfo = MutableLiveData<Resource<QuestionAdditionalInfo>>()
    val questionAdditionalInfo: LiveData<Resource<QuestionAdditionalInfo>>
        get() = _questionAdditionalInfo

    private val _questionQuantityAnswered = MutableLiveData<Int>()
    val questionQuantityAnswered: LiveData<Int>
        get() = _questionQuantityAnswered

    private val _questionAverageCorrects = MutableLiveData<Double>()
    val questionAverageCorrects: LiveData<Double>
        get() = _questionAverageCorrects

    private var answerPercentageMap = hashMapOf<String, Double>()

    fun setCurrentTeacherGrade(grade: GradeEnum) {
        currentTeacherGrade = grade
    }

    fun getCurrentTeacherGrade() = currentTeacherGrade

    fun setCurrentQuestion(question: Question) {
        currentQuestion = question
    }

    fun clearCurrentQuestion() {
        currentQuestion = null
    }

    fun clearViewModel() {
        currentTeacherGrade = GradeEnum.UNKOWN
        currentQuestion = null
        answerPercentageMap = hashMapOf()
        ioScope.launch {
            _teacherQuestions.postValue(null)
            _questionAdditionalInfo.postValue(null)
            _questionQuantityAnswered.postValue(null)
            _questionAverageCorrects.postValue(null)
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
                                val filteredQuestions =
                                    questions.filter { it.questionGrade == currentTeacherGrade }
                                _teacherQuestions.postValue(Resource.success(filteredQuestions))
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

    fun fetchQuestionAdditionalInfo() {
        ioScope.launch {
            _questionAdditionalInfo.postValue(Resource.loading())
            currentQuestion?.let { itCurrentQuestion ->
                firebaseRepo.fetchQuestionAdditionalInfoByQuestionId(
                    itCurrentQuestion.id,
                    object : SingleQuestionAddInfoCallback {
                        override fun onResponse(data: QuestionAdditionalInfo?) {
                            data?.let {
                                calculateQuestionData(it)
                                _questionAdditionalInfo.postValue(Resource.success(it))
                            } ?: run {
                                _questionAdditionalInfo.postValue(
                                    Resource.error(
                                        "Erro ao buscar informações da pergunta.",
                                        null
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun calculateQuestionData(currentQuestionAddInfo: QuestionAdditionalInfo) {
        _questionQuantityAnswered.postValue(currentQuestionAddInfo.timesAnswered)
        val averageCorrects = calculateAverageCorrects(currentQuestionAddInfo)
        _questionAverageCorrects.postValue(averageCorrects)
        calculateAnswersAdditionalInfo(currentQuestionAddInfo)
    }

    private fun calculateAverageCorrects(currentQuestionAddInfo: QuestionAdditionalInfo): Double {
        val timesAnswered = currentQuestionAddInfo.timesAnswered
        val amountCorrect =
            currentQuestionAddInfo.answersAdditionalInfo.find { it.correctAnswer }?.timesAnswered
                ?: 1
        return if (amountCorrect == 0) {
            amountCorrect.toDouble()
        } else {
            (amountCorrect.toDouble() / timesAnswered.toDouble() ) * 100
        }
    }

    private fun calculateAnswersAdditionalInfo(currentQuestionAddInfo: QuestionAdditionalInfo) {
        val totalAnswers = currentQuestionAddInfo.timesAnswered
        currentQuestionAddInfo.answersAdditionalInfo.forEach {
            val currentAnswer = it
            answerPercentageMap[it.answerId] = if (it.timesAnswered == 0 || totalAnswers == 0) {
                0.0
            } else {
                (it.timesAnswered.toDouble() / totalAnswers.toDouble()) * 100
            }
        }
    }

    fun getAnswerPercentage(answerId: String): Double {
        return answerPercentageMap[answerId] ?: 0.0
    }

    fun getCurrentQuestion() = currentQuestion

}