package com.example.pucquiz.ui.quizhall.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.overrideOnBackPressed
import kotlinx.android.synthetic.main.fragment_question_creation.*

class QuestionCreationFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_creation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupListeners()
        setupRadioGroup()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            validateOnBackPressed()
        }

        materialButton_register_question.setOnClickListener {
            validateQuestionForm()
        }

        imageView_summary_hint.setOnClickListener {
            showSummaryHintDialog()
        }

        imageView_alternatives_hint.setOnClickListener {
            showAlternativesHintDialog()
        }
    }

    private fun showSummaryHintDialog() {
        showDialog(
            title = "Enunciado",
            description = "No Enunciado, você descreve a pergunta que deve ser feita ao aluno.",
            confirm = "Entendi",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun showAlternativesHintDialog() {
        showDialog(
            title = "Alternativas",
            description = "Utilize o círuclo à esquerda para determinar qual a alternativa correta! Essas serão as alternativas que o aluno tera como opçnão de marcar no questionário.",
            confirm = "Entendi",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun setupRadioGroup() {
        first_option.setOnClickListener {
            changeRadioGroupState(first_option.id)
        }
        second_option.setOnClickListener {
            changeRadioGroupState(second_option.id)
        }
        third_option.setOnClickListener {
            changeRadioGroupState(third_option.id)
        }
        forth_option.setOnClickListener {
            changeRadioGroupState(forth_option.id)
        }
        fifth_option.setOnClickListener {
            changeRadioGroupState(fifth_option.id)
        }
    }

    private fun changeRadioGroupState(radioButtonId: Int) {
        val radioGroupList =
            listOf<RadioButton>(
                first_option,
                second_option,
                third_option,
                forth_option,
                fifth_option
            )
        radioGroupList.forEach {
            it.isChecked = it.id == radioButtonId
        }
    }

    private fun validateOnBackPressed() {
        if (checkFields()) {
            showDialog(
                title = "Atenção",
                description = "Caso decida voltar, todos os campos escritos serão descartados. Deseja continuar?",
                confirm = "Sim",
                cancel = "Não"
            ) {
                listener?.backToQuestionsHall()
            }
        } else {
            listener?.backToQuestionsHall()
        }
    }

    private fun validateQuestionForm() {
        if (checkFields()) {
            createQuestion()
        }
    }

    private fun createQuestion() {

    }

    private fun checkFields(): Boolean {
        return editText_question_summary.text.toString().trim()
            .isNotBlank() || editText_first_question.text.toString().trim().isNotBlank() ||
                editText_second_question.text.toString().trim()
                    .isNotBlank() || editText_third_question.text.toString().trim().isNotBlank() ||
                editText_forth_question.text.toString().trim()
                    .isNotBlank() || editText_fifth_question.text.toString().trim().isNotBlank()
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

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            validateOnBackPressed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun backToQuestionsHall()
    }
}