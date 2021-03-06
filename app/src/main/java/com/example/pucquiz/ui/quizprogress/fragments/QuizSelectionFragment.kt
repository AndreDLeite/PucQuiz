package com.example.pucquiz.ui.quizprogress.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.quizprogress.viewmodels.QuizMainViewModel
import com.example.pucquiz.ui.shared.enums.QuizType
import kotlinx.android.synthetic.main.fragment_question_selection.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizSelectionFragment : Fragment() {

    private val quizConfigurationViewModel by sharedViewModel<QuizMainViewModel>()

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
    }

    private fun setupListeners() {
        constraintLayout_questions_enade.setOnClickListener {
            quizConfigurationViewModel.setQuizType(QuizType.ENADE)
            listener?.onQuizTypeClicked()
        }

        constraintLayout_questions_teacher.setOnClickListener {
            quizConfigurationViewModel.setQuizType(QuizType.TEACHER)
            listener?.onQuizTypeClicked()
        }
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
        fun onQuizTypeClicked()
    }
}