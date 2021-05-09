package com.example.pucquiz.ui.quizprogress.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.quizprogress.viewmodels.QuizMainViewModel
import kotlinx.android.synthetic.main.fragment_quiz_calculations.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizCalculationsFragment : Fragment() {

    private var listener: OnFragmentOperationListener? = null
    private val quizConfigurationViewModel by sharedViewModel<QuizMainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_quiz_calculations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
        setupViewModelObservers()
        tryRegisterQuizResults()
    }

    private fun setupListeners() {
        materialButton_try_again.setOnClickListener {
            tryRegisterQuizResults()
        }
    }

    private fun tryRegisterQuizResults() {
        showLoadingViewState()
        quizConfigurationViewModel.launchFinishQuizRoutine()
    }

    private fun setupViewModelObservers() {
        quizConfigurationViewModel.quizUpdateOperations.observe(
            viewLifecycleOwner,
            Observer { itResource ->
                itResource ?: return@Observer
                when (itResource.status) {
                    Resource.Status.SUCCESS -> {
                        Handler().postDelayed({
                            listener?.onUserDataUpdatesFinished()
                        }, 1000)
                    }
                    Resource.Status.ERROR -> {
                        itResource.message?.let {
                            showErrorViewState(it)
                        }
                    }
                    Resource.Status.LOADING -> {
                        showLoadingViewState()
                    }
                }
            })
    }

    private fun showErrorViewState(errorMessage: String) {
        group_progress.visibility = View.GONE
        group_error.visibility = View.VISIBLE
        textView_error_description.text = errorMessage
    }

    private fun showLoadingViewState() {
        group_progress.visibility = View.VISIBLE
        group_error.visibility = View.GONE
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentOperationListener) {
            listener = context
        }
    }

    interface OnFragmentOperationListener {
        fun onUserDataUpdatesFinished()
    }
}