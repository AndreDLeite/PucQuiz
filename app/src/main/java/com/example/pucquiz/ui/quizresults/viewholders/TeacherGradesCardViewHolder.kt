package com.example.pucquiz.ui.quizresults.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_teacher_grades.view.*

class TeacherGradesCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val gradeName = itemView.textView_teacher_grades_title
    val container = itemView.constraintLayout_teacher_grades_container

}