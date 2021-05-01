package com.example.pucquiz.ui.quizhall.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_questions_resume.view.*

class QuestionCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container = itemView.constraintLayout_grade_selected_container
    var description = itemView.textView_question_title
    var questionGrade = itemView.textView_question_grade
}