package com.example.pucquiz.ui.quizselector.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import com.example.pucquiz.ui.medals.viewholders.MedalCardViewHolder
import com.example.pucquiz.ui.quizselector.viewholders.QuestionGradeViewHolder

class QuestionGradeDelegate {

    private var questionCardListener: OnQuestionGradeCardClicked? = null

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return QuestionGradeViewHolder(
            inflater.inflate(
                R.layout.item_question_grade,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        userGrade: Grade
    ) {
        with(holder as QuestionGradeViewHolder) {
            gradeName.text = userGrade.gradeType
            container.setOnClickListener {
                questionCardListener?.onQuestionCardClicked(userGrade)
            }
        }
    }

    fun setQuestionGradeListener(listener: OnQuestionGradeCardClicked) {
        questionCardListener = listener
    }

    companion object {
        const val VIEW_USER_GRADE_CARD = 340
    }

    interface OnQuestionGradeCardClicked {
        fun onQuestionCardClicked(currentGrade: Grade)
    }
}