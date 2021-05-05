package com.example.pucquiz.callbacks

import com.example.pucquiz.models.Question

interface FirebaseGradeQuestionsCallBack {

    fun onResponse(questions: List<Question>?)

}