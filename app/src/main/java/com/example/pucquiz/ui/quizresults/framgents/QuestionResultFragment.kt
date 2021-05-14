package com.example.pucquiz.ui.quizresults.framgents

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
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.Question
import com.example.pucquiz.shared.Resource
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
        callServices()
        updateGradeText()
        overrideOnBackPressed()
        setupListeners()
        setupViewModelObservers()
    }

    private fun updateGradeText() {
        val gradeString = GradeController().gradeToString(questionsResultViewModel.getCurrentTeacherGrade())
        textVIew_question_grade.text = gradeString
    }

    private fun callServices() {
        questionsResultViewModel.fetchQuestionAdditionalInfo()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            onBackPressedRoutine()
        }
    }

    private fun setupViewModelObservers() {
        questionsResultViewModel.questionAdditionalInfo.observe(
            viewLifecycleOwner,
            Observer { itResource ->
                itResource ?: return@Observer

                when (itResource.status) {
                    Resource.Status.SUCCESS -> {
                        removeLoadingState()
                        validateCurrentQuestion()
                    }
                    Resource.Status.ERROR -> {
                        removeLoadingState()
                    }
                    Resource.Status.LOADING -> {
                        setViewLoadingState()
                    }
                }
            })

        questionsResultViewModel.questionAverageCorrects.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            updateAverageText(it)

        })

        questionsResultViewModel.questionQuantityAnswered.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            updateAmountAnsweredText(it)
        })
    }

    private fun validateCurrentQuestion() {
        questionsResultViewModel.getCurrentQuestion()?.let {
            populateQuestionFields(it)
        } ?: run {
            showErrorLoadingCurrentQuestionDialog()
        }
    }

    private fun populateQuestionFields(currentQuestion: Question) {
        populateQuestionSummary(currentQuestion.summary)
        populateQuestionAnswers(currentQuestion.answers)
    }

    private fun populateQuestionSummary(questionSummary: String) {
        textView_question_result_summary.text = questionSummary
    }

    private fun populateQuestionAnswers(questionAnswers: List<Answer>) {
        questionAnswers.forEachIndexed { index, answer ->
            val correctAnswer = answer.correctAnswer
            val answerPercentage = questionsResultViewModel.getAnswerPercentage(answer.id)
            when(index) {
                0 -> {
                    question_first_option.text = answer.summary
                    question_first_option.isChecked = correctAnswer
                    textView_first_option_info.text = String.format("%.1f%% das respostas foram na alternativa abaixo", answerPercentage)
                }

                1 -> {
                    question_second_option.text = answer.summary
                    question_second_option.isChecked = correctAnswer
                    textView_second_option_info.text = String.format("%.1f%% das respostas foram na alternativa abaixo", answerPercentage)
                }

                2 -> {
                    question_third_option.text = answer.summary
                    question_third_option.isChecked = correctAnswer
                    textView_third_option_info.text = String.format("%.1f%% das respostas foram na alternativa abaixo", answerPercentage)
                }

                3 -> {
                    question_forth_option.text = answer.summary
                    question_forth_option.isChecked = correctAnswer
                    textView_forth_option_info.text = String.format("%.1f%% das respostas foram na alternativa abaixo", answerPercentage)
                }

                4 -> {
                    question_fifth_option.text = answer.summary
                    question_fifth_option.isChecked = correctAnswer
                    textView_fifth_option_info.text = String.format("%.1f%% das respostas foram na alternativa abaixo", answerPercentage)
                }

                else -> {
                    //ignore
                }
            }
        }
    }

    private fun showErrorLoadingCurrentQuestionDialog() {
        showDialog(
            title = "Oops",
            description = "Tivemos um problema para carregar informaçnoes desta questão em especifico. Por favor, tente novamente mais tarde.",
            confirm = "Ok",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun updateAverageText(average: Double) {
        textVIew_question_grade_total_average.text = String.format("%.2f%%", average)
    }

    private fun updateAmountAnsweredText(amountAnswered: Int) {
        textVIew_question_grade_total_answered.text = amountAnswered.toString()
    }

    private fun removeLoadingState() {
        group_loading.visibility = View.GONE
    }

    private fun setViewLoadingState() {
        group_loading.visibility = View.VISIBLE
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
        fun onBackToQuestionResultHall()
    }
}