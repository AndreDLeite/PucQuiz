package com.example.pucquiz.ui.quizprogress.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.Question
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.quizprogress.viewmodels.QuizMainViewModel
import kotlinx.android.synthetic.main.fragment_question_creation.*
import kotlinx.android.synthetic.main.fragment_quiz_progress.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizMainFragment : Fragment() {

    private val quizConfigurationViewModel by sharedViewModel<QuizMainViewModel>()

    private val answersHash = hashMapOf<RadioButton, Answer>()

    private var listener: OnFragmentRoutinesListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_quiz_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        loadFirstQuestion()
        setupListeners()
        setupViewModelObservers()
    }

    private fun loadFirstQuestion() {
        quizConfigurationViewModel.shuffleQuiz()
        quizConfigurationViewModel.getNextQuestion()
    }

    private fun setupListeners() {
        materialButton_next_question.setOnClickListener {
            validateAnswerSelected()
        }
    }

    private fun validateAnswerSelected() {
        val radioGroupList =
            listOf<RadioButton>(
                question_first_option,
                question_second_option,
                question_third_option,
                question_forth_option,
                question_fifth_option
            )
        if (radioGroupList.any { it.isChecked }) {
            val radioButtonAnswer = radioGroupList.find { it.isChecked }
            val markedAnswer = answersHash[radioButtonAnswer]
            quizConfigurationViewModel.addAnswerToQuestion(markedAnswer)
            radioGroup_answers.clearCheck()
            onNextQuestion()
        } else {
            showNoAnswerSelectedDialog()
        }
    }

    private fun showNoAnswerSelectedDialog() {
        showDialog(
            title = "Aviso!",
            description = "Selecione uma opção nas alternativas para poder prosseguir!",
            confirm = "Entendi",
            cancel = null
        ) {

        }
    }

    private fun onNextQuestion() {
        quizConfigurationViewModel.getNextQuestion()
    }

    private fun setupLastQuestionViewState() {
        materialButton_next_question.text = "Finalizar"
    }

    private fun setupViewModelObservers() {
        quizConfigurationViewModel.currentQuestion.observe(
            viewLifecycleOwner,
            Observer { itResource ->
                itResource ?: return@Observer

                when (itResource.status) {
                    Resource.Status.SUCCESS -> {
                        showFormViewState()
                        updateView()
                        itResource.data?.let {
                            populateQuizFields(it)
                        }
                    }
                    Resource.Status.ERROR -> {

                    }
                    Resource.Status.LOADING -> {
                        showLoadingViewState()
                    }
                }
            })

        quizConfigurationViewModel.isQuizOver.observe(viewLifecycleOwner, Observer { itBoolean ->
            itBoolean ?: return@Observer
            if (itBoolean) {
                Handler().postDelayed({
//                    quizConfigurationViewModel.clearViewModel()
                    listener?.onQuizFinish()
                }, 1000)
            }
        })
    }

    private fun showFormViewState() {
        group_quiz_confirm.visibility = View.VISIBLE
        progress_bar_quiz_loading.visibility = View.GONE
    }

    private fun showLoadingViewState() {
        group_quiz_confirm.visibility = View.GONE
        progress_bar_quiz_loading.visibility = View.VISIBLE
    }

    private fun populateQuizFields(currentQuestion: Question) {
        textView_question_summary.text = currentQuestion.summary
        currentQuestion.answers.forEachIndexed { index, answer ->
            when (index) {
                0 -> {
                    answersHash[question_first_option] = answer
                    question_first_option.text = answer.summary
                }
                1 -> {
                    answersHash[question_second_option] = answer
                    question_second_option.text = answer.summary
                }
                2 -> {
                    answersHash[question_third_option] = answer
                    question_third_option.text = answer.summary
                }
                3 -> {
                    answersHash[question_forth_option] = answer
                    question_forth_option.text = answer.summary
                }
                4 -> {
                    answersHash[question_fifth_option] = answer
                    question_fifth_option.text = answer.summary
                }
            }
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

    private fun updateView() {
        val questionCount = quizConfigurationViewModel.getCurrentCount()
        textView_quiz_headerTitle.text = String.format("Pergunta %d de 5", questionCount)
        if (questionCount == 5) {
            setupLastQuestionViewState()
        }
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentRoutinesListener) {
            listener = context
        }
    }

    interface OnFragmentRoutinesListener {
        fun onQuizFinish()
    }

}