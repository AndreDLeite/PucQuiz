package com.example.pucquiz.ui.quizselector.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.ui.shared.enums.QuizType

class QuizConfigurationViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
): AndroidViewModel(application) {

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

}