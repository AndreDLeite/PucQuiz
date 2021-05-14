package com.example.pucquiz.controllers

import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.shared.enums.QuizType
import kotlinx.coroutines.processNextEventInCurrentThread
import java.util.*
import kotlin.collections.HashMap

class QuestionController {

    fun generateQuestion(
        teacherId: String,
        summary: String,
        answerList: HashMap<String, Boolean>,
        questionGrade: GradeEnum,
        questionType: QuizType
    ): Question {
        val realAnswerList = mutableListOf<Answer>()
        answerList.forEach {
            val answerId = UUID.randomUUID().toString()
            realAnswerList.add(
                Answer(
                    answerId,
                    it.key,
                    it.value
                )
            )
        }
        val questionId = UUID.randomUUID().toString()
        return Question(
            id = questionId,
            teacherId = teacherId,
            summary = summary,
            answers = realAnswerList,
            questionGrade = questionGrade,
            questionType = questionType
        )
    }

    fun generateUpdatedQuestion(
        teacherId: String,
        summary: String,
        answerList: List<Answer>,
        newQuestionData: Question
    ): Question {
        return Question(
            id = newQuestionData.id,
            teacherId = teacherId,
            summary = summary,
            answers = answerList,
            questionGrade = newQuestionData.questionGrade,
            questionType = newQuestionData.questionType
        )
    }

}