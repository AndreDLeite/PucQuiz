package com.example.pucquiz.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    private fun setupListeners() {
        buttonRedes.setOnClickListener {
            navigateToRedesQuiz()
        }

        buttonHibridas.setOnClickListener {
            navigateToHibridasQuiz()
        }

        buttonHDistribuidas.setOnClickListener {
            navigateToDistribuidasQuiz()
        }
    }

    private fun navigateToRedesQuiz() {

    }

    private fun navigateToDistribuidasQuiz() {

    }

    private fun navigateToHibridasQuiz() {

    }

    interface OnFragmentInteractionListener {

        fun onRedesQuizSelected()

        fun onDistribuidasQuizSelected()

        fun onHibridasQuizSelected()
    }


}