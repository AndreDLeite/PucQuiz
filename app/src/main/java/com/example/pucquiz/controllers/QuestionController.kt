package com.example.pucquiz.controllers

import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.Question

class QuestionController {

    fun generateQuestion(
        teacherId: String,
        summary: String,
        answerList: HashMap<String, Boolean>,
        questionGrade: GradeEnum
    ): Question {
        val realAnswerList = mutableListOf<Answer>()
        answerList.forEach {
            realAnswerList.add(
                Answer(
                    it.key,
                    it.value
                )
            )
        }
        return Question(
            teacherId = teacherId,
            summary = summary,
            answers = realAnswerList,
            questionType = questionGrade
        )
    }

}