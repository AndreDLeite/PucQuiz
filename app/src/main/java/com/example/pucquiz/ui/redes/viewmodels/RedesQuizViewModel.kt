package com.example.pucquiz.ui.redes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.data.remote.QuestionsServiceFactory
import com.example.pucquiz.models.QuestionItem
import com.example.pucquiz.models.QuestionType
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RedesQuizViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val _questionsLiveData = MutableLiveData<List<QuestionItem>>()
    val questionsLiveData: LiveData<List<QuestionItem>>
        get() = _questionsLiveData

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        QuestionsServiceFactory.makeService().getQuestionsList()
            .enqueue(object : Callback<List<QuestionItem>?> {
                override fun onFailure(call: Call<List<QuestionItem>?>, t: Throwable) {
                    _questionsLiveData.postValue(null)
                }

                override fun onResponse(
                    call: Call<List<QuestionItem>?>,
                    response: Response<List<QuestionItem>?>
                ) {
                    val questions = response.body()
                    questions?.let {
                        val filteredQuestions = it.filter { it.questionType == QuestionType.REDES }
                        _questionsLiveData.postValue(filteredQuestions)
                    }
                }
            })
    }
}