package com.example.pucquiz.ui.quizhall.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.quizhall.viewholders.QuestionCardViewHolder

class QuestionCardDelegate {

    private var questionCardListener: OnQuestionCardClicked? = null

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return QuestionCardViewHolder(
            inflater.inflate(
                R.layout.item_questions_resume,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        question: Question
    ) {
        with(holder as QuestionCardViewHolder) {
            val gradeType = GradeController().gradeToString(question.questionType)
            description.text = question.summary
            questionGrade.text = gradeType
            container.setOnClickListener {
                //TODO: Call listener here
            }
        }
    }

    fun setQuestionCardListener(listener: OnQuestionCardClicked) {
        questionCardListener = listener
    }

    companion object {
        const val VIEW_QUESTIONS_CARD = 120
    }

    interface OnQuestionCardClicked {
        fun onQuestionCardClicked(question: Question)
    }
}