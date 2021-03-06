package com.example.pucquiz.ui.redes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.ui.redes.viewmodels.RedesQuizViewModel
import kotlinx.android.synthetic.main.fragment_redes_quiz.*
import org.koin.android.ext.android.inject

class RedesQuizFragment : Fragment() {

    private val viewModel by inject<RedesQuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_redes_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewModelObservers()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        buttonStartQuiz.setOnClickListener {
            //TODO: load fragment with questions loaded from server
        }
    }

    private fun setupViewModelObservers() {
        viewModel.questionsLiveData.observe(viewLifecycleOwner, Observer { questionList ->
            questionList ?: return@Observer
            setViewToSuccessState()
        })
    }

    private fun setViewToSuccessState() {
        loadingQuestions.visibility = View.GONE
        buttonStartQuiz.visibility = View.VISIBLE
    }

}