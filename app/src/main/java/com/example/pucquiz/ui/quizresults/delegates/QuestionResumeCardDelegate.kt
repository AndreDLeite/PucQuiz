package com.example.pucquiz.ui.quizresults.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.controllers.QuizTypeController
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.quizresults.viewholders.QuestionResumeCardViewHolder
import com.example.pucquiz.ui.shared.enums.QuizType

class QuestionResumeCardDelegate {

    private var questionCardListener: OnQuestionResumeCardInteraction? = null

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return QuestionResumeCardViewHolder(
            inflater.inflate(
                R.layout.item_questions_result_resume,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        question: Question
    ) {
        with(holder as QuestionResumeCardViewHolder) {
            val gradeType = GradeController().gradeToString(question.questionGrade)
            description.text = question.summary
            questionGrade.text = gradeType
            autority.text = QuizTypeController().quizTypeToString(question.questionType)
            container.setOnClickListener {
                questionCardListener?.onQuestionCardClicked(question)
            }
        }
    }

    fun setQuestionCardListener(listener: OnQuestionResumeCardInteraction) {
        questionCardListener = listener
    }

    companion object {
        const val VIEW_QUESTION_RESUME_CARD = 122
    }

    interface OnQuestionResumeCardInteraction {
        fun onQuestionCardClicked(question: Question)
    }
}