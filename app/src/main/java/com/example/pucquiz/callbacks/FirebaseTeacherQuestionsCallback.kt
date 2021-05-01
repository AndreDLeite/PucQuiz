package com.example.pucquiz.callbacks

import com.example.pucquiz.models.Question

interface FirebaseTeacherQuestionsCallback {

    fun onResponse(questions: List<Question>?)

}