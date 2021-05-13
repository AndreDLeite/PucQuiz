package com.example.pucquiz.ui.quizresults.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_questions_result_resume.view.*

class QuestionResumeCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val container = itemView.constraintLayout_grade_selected_container
    val questionGrade = itemView.textView_question_result_grade
    val description = itemView.textView_question_title
}