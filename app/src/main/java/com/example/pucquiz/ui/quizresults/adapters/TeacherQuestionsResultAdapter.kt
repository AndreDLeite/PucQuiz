package com.example.pucquiz.ui.quizresults.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.quizresults.delegates.QuestionResumeCardDelegate
import com.example.pucquiz.ui.quizresults.delegates.QuestionResumeCardDelegate.Companion.VIEW_QUESTION_RESUME_CARD
import com.example.pucquiz.ui.shared.delegates.StatusAdapterDelegate

class TeacherQuestionsResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var questionsList: MutableList<Question> = mutableListOf()

    private val statusAdaptedDelegate = StatusAdapterDelegate()
    private val questionsResumeCardDelegate = QuestionResumeCardDelegate()

    private var currentStatus: QuestionsStatus = QuestionsStatus.DEFAULT_TEACHER_QUESTION_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_QUESTION_RESUME_CARD -> {
                questionsResumeCardDelegate.onCreateViewHolder(
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
        when(holder.itemViewType) {
            VIEW_QUESTION_RESUME_CARD -> {
                questionsResumeCardDelegate.onBindViewHolder(
                    holder,
                    questionsList[position]
                )
            }
            else -> {
                statusAdaptedDelegate.onBindViewHolder()
            }
        }
    }

    override fun getItemCount(): Int {
       return totalItemCount(questionsList, currentStatus)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(currentStatus)
    }

    private fun totalItemCount(list: List<Question>, status: QuestionsStatus): Int {
        return if(status == QuestionsStatus.DEFAULT_TEACHER_QUESTION_LIST) list.size else 1
    }

    private fun itemViewType(status: QuestionsStatus): Int {
        return when(status) {
            QuestionsStatus.LOADING_TEACHER_QUESTION_LIST,
            QuestionsStatus.ERROR_TEACHER_QUESTION_LIST,
            QuestionsStatus.EMPTY_TEACHER_QUESTION_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            QuestionsStatus.DEFAULT_TEACHER_QUESTION_LIST -> {
                VIEW_QUESTION_RESUME_CARD
            }
        }
    }

    fun updateTeacherQuestionsList(currentTeacherQuestions: List<Question>) {
        currentStatus = if (currentTeacherQuestions.isEmpty()) {
            QuestionsStatus.EMPTY_TEACHER_QUESTION_LIST
        } else {
            QuestionsStatus.DEFAULT_TEACHER_QUESTION_LIST
        }

        questionsList.clear()
        questionsList.addAll(currentTeacherQuestions)
        notifyDataSetChanged()
    }

    fun updateStatus(status: QuestionsStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    fun setQuestionCardListener(listener: QuestionResumeCardDelegate.OnQuestionResumeCardInteraction) {
        questionsResumeCardDelegate.setQuestionCardListener(listener)
    }

    enum class QuestionsStatus {
        LOADING_TEACHER_QUESTION_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_TEACHER_QUESTION_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        EMPTY_TEACHER_QUESTION_LIST {
            override fun layout(): Int = R.layout.layout_empty_questions_result
        },
        DEFAULT_TEACHER_QUESTION_LIST {
            override fun layout(): Int = R.layout.item_questions_result_resume
        };

        abstract fun layout(): Int
    }
}