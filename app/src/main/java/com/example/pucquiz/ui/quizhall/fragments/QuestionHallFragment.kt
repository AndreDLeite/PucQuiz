package com.example.pucquiz.ui.quizhall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Question
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.quizhall.adapters.QuestionsAdapter
import com.example.pucquiz.ui.quizhall.delegate.QuestionCardDelegate
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionsHallViewModel
import kotlinx.android.synthetic.main.fragment_question_hall.*
import org.koin.android.ext.android.inject

class QuestionHallFragment : Fragment(), QuestionCardDelegate.OnQuestionCardClicked {

    private val questionsHallViewModel by inject<QuestionsHallViewModel>()

    private lateinit var questionsAdapter: QuestionsAdapter

    //recyclerView_questions
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_hall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callServices()
        initRecyclerView()
        setupListeners()
        setupViewModelObservers()
        overrideOnBackPressed()
    }

    private fun callServices() {
        questionsHallViewModel.fetchTeacherQuestions()
    }

    private fun initRecyclerView() {
        questionsAdapter = QuestionsAdapter()
        recyclerView_questions.adapter = questionsAdapter
        recyclerView_questions.layoutManager = LinearLayoutManager(activity)

        questionsAdapter.setQuestionCardListener(this)
    }

    private fun setupListeners() {
        add_question.setOnClickListener {

        }
    }

    private fun setupViewModelObservers() {
        questionsHallViewModel.teacherQuestions.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        questionsAdapter.updateQuestionsList(it)
                    }
                }
                Resource.Status.ERROR -> {
                    questionsAdapter.updateStatus(QuestionsAdapter.QuestionsStatus.ERROR_QUESTIONS_LIST)
                }
                Resource.Status.LOADING -> {
                    questionsAdapter.updateStatus(QuestionsAdapter.QuestionsStatus.LOADING_QUESTIONS_LIST)
                }
            }
        })
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    override fun onQuestionCardClicked(question: Question) {
        //TODO: Open same screen as the "quiz" but with the option to delete the question...
    }

    interface OnFragmentInteractionListener {
        fun onAddQuestionClicked()
    }
}