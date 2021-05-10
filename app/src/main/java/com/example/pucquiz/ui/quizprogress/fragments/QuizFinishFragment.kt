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
import kotlinx.android.synthetic.main.fragment_quiz_finish.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizFinishFragment : Fragment() {

    private val quizConfigurationViewModel by sharedViewModel<QuizMainViewModel>()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_quiz_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
    }

    private fun setupListeners() {
        materialButton_go_back.setOnClickListener {
            quizConfigurationViewModel.clearViewModel()
            listener?.onBackToOnBoarding()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackToOnBoarding()
    }

}