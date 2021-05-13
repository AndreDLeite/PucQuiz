package com.example.pucquiz.ui.quizresults.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import com.example.pucquiz.ui.quizresults.viewholders.TeacherGradesCardViewHolder

class TeacherGradesCardDelegate {

    private var teacherGradeCardListener: OnTeacherGradeCardListener? = null

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return TeacherGradesCardViewHolder(
            inflater.inflate(
                R.layout.item_teacher_grades,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        teacherGrade: Grade
    ) {
        with(holder as TeacherGradesCardViewHolder) {
            gradeName.text = teacherGrade.gradeType
            container.setOnClickListener {
                teacherGradeCardListener?.onTeacherGradeCardClicked(teacherGrade)
            }
        }
    }

    fun setTeacherGradeCardListener(listener: OnTeacherGradeCardListener) {
        teacherGradeCardListener = listener
    }

    companion object {
        const val VIEW_TEACHER_GRADES_CARD = 111
    }

    interface OnTeacherGradeCardListener {
        fun onTeacherGradeCardClicked(currentGrade: Grade)
    }
}