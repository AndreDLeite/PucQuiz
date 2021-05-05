package com.example.pucquiz.ui.quizselector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.quizhall.adapters.QuestionsAdapter
import com.example.pucquiz.ui.quizselector.delegates.QuestionGradeDelegate
import com.example.pucquiz.ui.shared.delegates.StatusAdapterDelegate

class QuestionGradeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userGrades: MutableList<Grade> = mutableListOf()

    private var currentStatus: QuestionGradeInfoStatus =
        QuestionGradeInfoStatus.DEFAULT_USER_GRADES_LIST

    private val questionGradeCardDelegate = QuestionGradeDelegate()
    private val statusAdaptedDelegate = StatusAdapterDelegate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            QuestionGradeDelegate.VIEW_USER_GRADE_CARD -> {
                questionGradeCardDelegate.onCreateViewHolder(
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
            QuestionGradeDelegate.VIEW_USER_GRADE_CARD -> {
                questionGradeCardDelegate.onBindViewHolder(
                    holder,
                    userGrades[position]
                )
            }

            else -> {
                statusAdaptedDelegate.onBindViewHolder()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(currentStatus)
    }

    override fun getItemCount(): Int {
        return totalItemCount(userGrades, currentStatus)
    }

    private fun totalItemCount(list: List<Grade>, status: QuestionGradeInfoStatus): Int {
        return if (status == QuestionGradeInfoStatus.DEFAULT_USER_GRADES_LIST) list.size else 1
    }

    fun updateQuestionsGrade(newGradesList: List<Grade>) {
        userGrades.clear()
        userGrades.addAll(newGradesList)
        notifyDataSetChanged()
    }

    fun updateStatus(status: QuestionGradeInfoStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    fun setQuestionGradeCardClickListener(listener: QuestionGradeDelegate.OnQuestionGradeCardClicked) {
        questionGradeCardDelegate.setQuestionGradeListener(listener)
    }

    private fun itemViewType(status: QuestionGradeInfoStatus): Int {
        return when (status) {
            QuestionGradeInfoStatus.LOADING_USER_GRADES_LIST,
            QuestionGradeInfoStatus.ERROR_USER_GRADES_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            QuestionGradeInfoStatus.DEFAULT_USER_GRADES_LIST -> {
                QuestionGradeDelegate.VIEW_USER_GRADE_CARD
            }
        }
    }

    enum class QuestionGradeInfoStatus {
        LOADING_USER_GRADES_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_USER_GRADES_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        DEFAULT_USER_GRADES_LIST {
            override fun layout(): Int = R.layout.item_question_grade
        };

        abstract fun layout(): Int
    }
}