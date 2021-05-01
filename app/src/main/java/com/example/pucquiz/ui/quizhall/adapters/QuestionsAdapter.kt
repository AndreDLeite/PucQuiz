package com.example.pucquiz.ui.quizhall.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Question
import com.example.pucquiz.ui.quizhall.delegate.QuestionCardDelegate
import com.example.pucquiz.ui.shared.delegates.StatusAdapterDelegate

class QuestionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var questionsList: MutableList<Question> = mutableListOf()

    private var currentStatus: QuestionsStatus =
        QuestionsStatus.DEFAULT_QUESTIONS_LIST

    private val questionCardDelegate = QuestionCardDelegate()
    private val statusAdaptedDelegate = StatusAdapterDelegate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            QuestionCardDelegate.VIEW_QUESTIONS_CARD -> {
                questionCardDelegate.onCreateViewHolder(
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

    fun setQuestionCardListener(listener: QuestionCardDelegate.OnQuestionCardClicked) {
        questionCardDelegate.setQuestionCardListener(listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            QuestionCardDelegate.VIEW_QUESTIONS_CARD -> {
                questionCardDelegate.onBindViewHolder(
                    holder,
                    questionsList[position]
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
        return totalItemCount(questionsList, currentStatus)
    }

    private fun totalItemCount(list: List<Question>, status: QuestionsStatus): Int {
        return if (status == QuestionsStatus.DEFAULT_QUESTIONS_LIST) list.size else 1
    }

    private fun itemViewType(status: QuestionsStatus): Int {
        return when (status) {
            QuestionsStatus.LOADING_QUESTIONS_LIST,
            QuestionsStatus.ERROR_QUESTIONS_LIST,
            QuestionsStatus.EMPTY_QUESTIONS_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            QuestionsStatus.DEFAULT_QUESTIONS_LIST -> {
                QuestionCardDelegate.VIEW_QUESTIONS_CARD
            }
        }
    }

    fun updateQuestionsList(newQuestionList: List<Question>) {
        currentStatus = if (newQuestionList.isEmpty()) {
            QuestionsStatus.EMPTY_QUESTIONS_LIST
        } else {
            QuestionsStatus.DEFAULT_QUESTIONS_LIST
        }
        questionsList.clear()
        questionsList.addAll(newQuestionList)
        notifyDataSetChanged()
    }

    fun updateStatus(status: QuestionsStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    enum class QuestionsStatus {
        LOADING_QUESTIONS_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_QUESTIONS_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        EMPTY_QUESTIONS_LIST {
            override fun layout(): Int = R.layout.layout_empty_status_questions
        },
        DEFAULT_QUESTIONS_LIST {
            override fun layout(): Int = R.layout.item_questions_resume
        };

        abstract fun layout(): Int
    }

}