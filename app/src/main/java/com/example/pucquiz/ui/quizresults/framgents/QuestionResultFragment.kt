package com.example.pucquiz.ui.quizresults.framgents

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.quizresults.viewmodels.QuestionsResultViewModel
import kotlinx.android.synthetic.main.fragment_question_result.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionResultFragment: Fragment() {

    private val questionsResultViewModel by sharedViewModel<QuestionsResultViewModel>()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
//        setupViewModelObservers()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            onBackPressedRoutine()
        }
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            onBackPressedRoutine()
        }
    }

    private fun onBackPressedRoutine() {
        questionsResultViewModel.clearCurrentQuestion()
        listener?.onBackToQuestionResultHall()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackToQuestionResultHall()
    }
}