package com.example.pucquiz.ui.quizresults.framgents

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Grade
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import com.example.pucquiz.ui.quizresults.adapters.TeacherGradesAdapter
import com.example.pucquiz.ui.quizresults.delegates.TeacherGradesCardDelegate
import com.example.pucquiz.ui.quizresults.viewmodels.QuestionsResultViewModel
import kotlinx.android.synthetic.main.fragment_questions_result_grade_selector.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionsResultGradeSelectionFragment : Fragment(),
    TeacherGradesCardDelegate.OnTeacherGradeCardListener {

    private val userInfoViewModel by sharedViewModel<UserInfoViewModel>()
    private val questionsResultViewModel by sharedViewModel<QuestionsResultViewModel>()

    private lateinit var teacherGradesAdapter: TeacherGradesAdapter

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_questions_result_grade_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setCardListener()
        overrideOnBackPressed()
        setupViewModelObservers()
    }

    private fun setCardListener() {
        teacherGradesAdapter.setTeacherGradeCardListener(this)
    }

    private fun initRecyclerView() {
        teacherGradesAdapter = TeacherGradesAdapter()
        recyclerView_questions_result_grade_list.adapter = teacherGradesAdapter
        recyclerView_questions_result_grade_list.layoutManager = LinearLayoutManager(activity)
    }

    private fun setupViewModelObservers() {
        userInfoViewModel.currentUserInfo.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        teacherGradesAdapter.updateTeacherGradesList(it.registered_grades)
                    }
                }
                Resource.Status.ERROR -> {
                    teacherGradesAdapter.updateStatus(TeacherGradesAdapter.TeacherGradeStatus.ERROR_TEACHER_GRADES_LIST)
                }
                Resource.Status.LOADING -> {
                    teacherGradesAdapter.updateStatus(TeacherGradesAdapter.TeacherGradeStatus.LOADING_TEACHER_GRADES_LIST)
                }
            }

        })
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onGradeCardClicked()
    }

    override fun onTeacherGradeCardClicked(currentGrade: Grade) {
        val gradeEnum = GradeController().gradeFromString(currentGrade.gradeType)
        questionsResultViewModel.setCurrentTeacherGrade(gradeEnum)
        listener?.onGradeCardClicked()
    }
}