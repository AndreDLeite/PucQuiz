package com.example.pucquiz.ui.quizresults.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.Medals
import com.example.pucquiz.ui.medals.adapters.MedalsAdapter
import com.example.pucquiz.ui.medals.delegates.MedalCardDelegate
import com.example.pucquiz.ui.quizresults.delegates.TeacherGradesCardDelegate
import com.example.pucquiz.ui.quizresults.delegates.TeacherGradesCardDelegate.Companion.VIEW_TEACHER_GRADES_CARD
import com.example.pucquiz.ui.shared.delegates.StatusAdapterDelegate

class TeacherGradesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var teacherGrades: MutableList<Grade> = mutableListOf()

    private var currentStatus: TeacherGradeStatus = TeacherGradeStatus.DEFAULT_TEACHER_GRADES_LIST

    private val teacherGradesCardDelegate = TeacherGradesCardDelegate()
    private val statusAdaptedDelegate = StatusAdapterDelegate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TEACHER_GRADES_CARD -> {
                teacherGradesCardDelegate.onCreateViewHolder(
                    inflater = LayoutInflater.from(parent.context),
                    parent = parent
                )
            }
            else -> {
                statusAdaptedDelegate.onCreateViewHolder(
                    inflater = LayoutInflater.from(parent.context),
                    parent = parent,
                    layoutToInflate = currentStatus.layout()
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TEACHER_GRADES_CARD -> {
                teacherGradesCardDelegate.onBindViewHolder(
                    holder,
                    teacherGrades[position]
                )
            }
            else -> {
                statusAdaptedDelegate.onBindViewHolder()
            }
        }
    }

    override fun getItemCount(): Int {
        return totalItemCount(teacherGrades, currentStatus)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(currentStatus)
    }

    private fun totalItemCount(list: List<Grade>, status: TeacherGradeStatus): Int {
        return if (status == TeacherGradeStatus.DEFAULT_TEACHER_GRADES_LIST) list.size else 1
    }

    fun updateTeacherGradesList(currentTeacherGrades: List<Grade>) {
        currentStatus = if (currentTeacherGrades.isEmpty()) {
            TeacherGradeStatus.EMPTY_TEACHER_GRADES_LIST
        } else {
            TeacherGradeStatus.DEFAULT_TEACHER_GRADES_LIST
        }

        teacherGrades.clear()
        teacherGrades.addAll(currentTeacherGrades)
        notifyDataSetChanged()
    }

    fun updateStatus(status: TeacherGradeStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    fun setTeacherGradeCardListener(listener: TeacherGradesCardDelegate.OnTeacherGradeCardListener) {
        teacherGradesCardDelegate.setTeacherGradeCardListener(listener)
    }

    private fun itemViewType(status: TeacherGradeStatus): Int {
        return when (status) {
            TeacherGradeStatus.LOADING_TEACHER_GRADES_LIST,
            TeacherGradeStatus.ERROR_TEACHER_GRADES_LIST,
            TeacherGradeStatus.EMPTY_TEACHER_GRADES_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            TeacherGradeStatus.DEFAULT_TEACHER_GRADES_LIST -> {
                VIEW_TEACHER_GRADES_CARD
            }
        }
    }

    enum class TeacherGradeStatus {
        LOADING_TEACHER_GRADES_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_TEACHER_GRADES_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        EMPTY_TEACHER_GRADES_LIST {
            override fun layout(): Int = R.layout.layout_empty_status
        },
        DEFAULT_TEACHER_GRADES_LIST {
            override fun layout(): Int = R.layout.item_teacher_grades
        };

        abstract fun layout(): Int
    }
}