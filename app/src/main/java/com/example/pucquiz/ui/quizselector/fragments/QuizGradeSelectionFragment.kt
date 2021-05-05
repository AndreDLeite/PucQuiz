package com.example.pucquiz.ui.quizselector.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.models.Grade
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import com.example.pucquiz.ui.quizhall.fragments.QuestionHallFragment
import com.example.pucquiz.ui.quizselector.adapters.QuestionGradeAdapter
import com.example.pucquiz.ui.quizselector.delegates.QuestionGradeDelegate
import com.example.pucquiz.ui.quizselector.viewmodels.QuizConfigurationViewModel
import kotlinx.android.synthetic.main.fragment_question_grade_selection.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizGradeSelectionFragment : Fragment(), QuestionGradeDelegate.OnQuestionGradeCardClicked {

    private var listener: OnFragmentInteractionListener? = null

    private val quizConfigurationViewModel by sharedViewModel<QuizConfigurationViewModel>()
    private val userInfoViewModel by sharedViewModel<UserInfoViewModel>()

    private lateinit var questionsGradeAdapter: QuestionGradeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_grade_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelsObservers()
        initRecyclerView()
    }

    private fun setupViewModelsObservers() {
        userInfoViewModel.currentUserInfo.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when(itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        questionsGradeAdapter.updateQuestionsGrade(it.registered_grades)
                    }
                }
                Resource.Status.ERROR -> {
                    questionsGradeAdapter.updateStatus(QuestionGradeAdapter.QuestionGradeInfoStatus.ERROR_USER_GRADES_LIST)
                }
                Resource.Status.LOADING -> {
                    questionsGradeAdapter.updateStatus(QuestionGradeAdapter.QuestionGradeInfoStatus.LOADING_USER_GRADES_LIST)
                }
            }
        })
    }

    private fun initRecyclerView() {
        questionsGradeAdapter = QuestionGradeAdapter()
        recyclerView_questions_grades.adapter = questionsGradeAdapter
        recyclerView_questions_grades.layoutManager = LinearLayoutManager(activity)

        questionsGradeAdapter.setQuestionGradeCardClickListener(this)
    }

    private fun setupQuizInfos(currentGrade: Grade) {
        val quizEnum = GradeController().gradeFromString(currentGrade.gradeType)
        quizConfigurationViewModel.setQuizGrade(quizEnum)
        listener?.onQuizSetup()
    }

    override fun onQuestionCardClicked(currentGrade: Grade) {
        setupQuizInfos(currentGrade)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onQuizSetup()
    }

}