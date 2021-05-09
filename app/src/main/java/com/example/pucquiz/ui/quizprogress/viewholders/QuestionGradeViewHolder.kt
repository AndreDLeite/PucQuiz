package com.example.pucquiz.ui.quizprogress.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question_grade.view.*

class QuestionGradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var container = itemView.constraintLayout_grade_selected_container
    var gradeName = itemView.textView_question_grade_title
}