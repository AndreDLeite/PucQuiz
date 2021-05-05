package com.example.pucquiz.ui.quizselector.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.quizselector.viewmodels.QuizConfigurationViewModel
import com.example.pucquiz.ui.welcome.WelcomeFragment
import kotlinx.android.synthetic.main.fragment_question_configuration.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizConfigurationFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private val quizConfigurationViewModel by sharedViewModel<QuizConfigurationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callServices()
        setupInitialOnBackPressed()
        setupViewModelObservers()
    }

    private fun callServices() {
        quizConfigurationViewModel.fetchGradeQuestions()
    }


    private fun setupInitialOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Add onBackPressed action here
        }
    }

    private fun setupViewModelObservers() {
        quizConfigurationViewModel.questions.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer
            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    showSuccessViewState()
                }
                Resource.Status.ERROR -> {
                    itResource.message?.let {
                        showErrorFetchQuestionsDialog(it)
                    }
                }
                Resource.Status.LOADING -> {
                    //ignore: initial state
                }
            }
        })
    }

    private fun showSuccessViewState() {
        restoreOnBackPressed()
        progress_bar.visibility = View.GONE
        group_quiz_confirm.visibility = View.VISIBLE
    }

    private fun restoreOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            listener?.onBackToQuestionsGradeSelection()
        }
    }

    private fun showErrorFetchQuestionsDialog(message: String) {
        showDialog(
            title = "Ooops...",
            description = message,
            confirm = "Ok",
            cancel = null
        ) {
            quizConfigurationViewModel.clearViewModel()
            listener?.onBackToQuestionsGradeSelection()
        }
    }

    private fun showDialog(
        title: String,
        description: String,
        confirm: String,
        cancel: String?,
        listener: () -> Unit
    ) {
        val dialog = DialogSimple()

        dialog.setDialogParameters(
            title = title,
            description = description,
            confirmText = confirm,
            cancelText = cancel
        )

        dialog.setDialogListener(object : DialogSimple.SimpleDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                dialog.dismiss()
                Handler().postDelayed(listener, 500)
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                dialog.dismiss()
            }
        })

        activity?.run {
            dialog.show(supportFragmentManager, DialogSimple::class.java.simpleName)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackToQuestionsGradeSelection()
    }
}
