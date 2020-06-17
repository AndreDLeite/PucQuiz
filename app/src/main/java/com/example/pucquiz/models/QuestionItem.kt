package com.example.pucquiz.models

data class QuestionItem(var name: String = "",
                        var isAvailable: Boolean = false,
                        var question: String = "",
                        var questionType: QuestionType = QuestionType.UNKNOWN,
                        var id: Long = 0)
