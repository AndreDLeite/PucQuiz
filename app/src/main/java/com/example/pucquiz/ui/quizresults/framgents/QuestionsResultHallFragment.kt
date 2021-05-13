package com.example.pucquiz.ui.quizresults.framgents

import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.example.pucquiz.ui.quizresults.adapters.TeacherQuestionsResultAdapter
import com.example.pucquiz.ui.quizresults.delegates.QuestionResumeCardDelegate
import com.example.pucquiz.ui.quizresults.viewmodels.QuestionsResultViewModel
import kotlinx.android.synthetic.main.fragment_question_hall.*
import kotlinx.android.synthetic.main.fragment_question_result_hall.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionsResultHallFragment : Fragment(),
    QuestionResumeCardDelegate.OnQuestionResumeCardInteraction {

    private var listener: OnFragmentInteractionListener? = null
    private val questionsResultViewModel by sharedViewModel<QuestionsResultViewModel>()

    private lateinit var questionsResultAdapter: TeacherQuestionsResultAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_result_hall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
        callServices()
        initRecyclerView()
        setupViewModelObservers()
        setCardListener()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            onBackPressedRoutine()
        }
    }

    private fun callServices() {
        questionsResultViewModel.fetchTeacherQuestions()
    }

    private fun initRecyclerView() {
        questionsResultAdapter = TeacherQuestionsResultAdapter()
        recyclerView_questions_result_resume.adapter = questionsResultAdapter
        recyclerView_questions_result_resume.layoutManager = LinearLayoutManager(activity)
    }

    private fun setupViewModelObservers() {
        questionsResultViewModel.teacherQuestions.observe(
            viewLifecycleOwner,
            Observer { itResource ->
                itResource ?: return@Observer

                when (itResource.status) {
                    Resource.Status.SUCCESS -> {
                        itResource.data?.let {
                            questionsResultAdapter.updateTeacherQuestionsList(it)
                            if (it.isEmpty()) {
                                constraint_questions_result_hint.visibility = View.GONE
                            } else {
                                constraint_questions_result_hint.visibility = View.VISIBLE
                            }
                        }
                    }
                    Resource.Status.ERROR -> {
                        questionsResultAdapter.updateStatus(TeacherQuestionsResultAdapter.QuestionsStatus.ERROR_TEACHER_QUESTION_LIST)
                    }
                    Resource.Status.LOADING -> {
                        questionsResultAdapter.updateStatus(TeacherQuestionsResultAdapter.QuestionsStatus.LOADING_TEACHER_QUESTION_LIST)
                    }
                }
            })
    }

    private fun setCardListener() {
        questionsResultAdapter.setQuestionCardListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            onBackPressedRoutine()
        }
    }

    private fun onBackPressedRoutine() {
        questionsResultViewModel.clearViewModel()
        Handler().postDelayed({
            listener?.onBackToTeacherGradeSelection()
        }, 500)
    }

    interface OnFragmentInteractionListener {
        fun onBackToTeacherGradeSelection()
        fun onQuestionCardClicked()
    }

    override fun onQuestionCardClicked(question: Question) {
        questionsResultViewModel.setCurrentQuestion(question)
        Handler().postDelayed({
            listener?.onQuestionCardClicked()
        }, 500)
    }

}