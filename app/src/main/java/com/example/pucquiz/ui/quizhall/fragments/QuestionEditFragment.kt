package com.example.pucquiz.ui.quizhall.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Answer
import com.example.pucquiz.models.Question
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionsHallViewModel
import kotlinx.android.synthetic.main.fragment_question_creation.*
import kotlinx.android.synthetic.main.fragment_question_edit.*
import kotlinx.android.synthetic.main.fragment_question_edit.backButton
import kotlinx.android.synthetic.main.fragment_question_edit.fifth_option
import kotlinx.android.synthetic.main.fragment_question_edit.first_option
import kotlinx.android.synthetic.main.fragment_question_edit.forth_option
import kotlinx.android.synthetic.main.fragment_question_edit.second_option
import kotlinx.android.synthetic.main.fragment_question_edit.third_option
import kotlinx.android.synthetic.main.fragment_question_result.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionEditFragment : Fragment() {

    private val questionsHallViewModel by sharedViewModel<QuestionsHallViewModel>()

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        overrideOnBackPressed()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        questionsHallViewModel.currentSelectedQuestion.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            populateQuestionFields(it)
        })

        questionsHallViewModel.updateOperationStatus.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    showSuccessUpdateDialog()
                }
                Resource.Status.ERROR -> {
                    showErrorUpdateDialog()
                }
                Resource.Status.LOADING -> {
                    showLoadingViewState()
                }
            }
        })

        questionsHallViewModel.deleteOperationStatus.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    showSuccessDeleteDialog()
                }
                Resource.Status.ERROR -> {
                    showErrorUpdateDialog()
                }
                Resource.Status.LOADING -> {
                    showLoadingViewState()
                }
            }
        })
    }

    private fun showSuccessDeleteDialog() {
        showDialog(
            title = "Tudo certo!",
            description = "Sua pergunta foi deletada com sucesso!",
            confirm = "Ok",
            cancel = null
        ) {
            questionsHallViewModel.clearViewModel()
            listener?.onBackToTeacherQuestionsList()
        }
    }

    private fun showSuccessUpdateDialog() {
        showDialog(
            title = "Tudo certo!",
            description = "Sua pergunta foi atualizada com sucesso!",
            confirm = "Ok",
            cancel = null
        ) {
            questionsHallViewModel.clearViewModel()
            listener?.onBackToTeacherQuestionsList()
        }
    }

    private fun showErrorUpdateDialog() {
        showDialog(
            title = "Oops",
            description = "Tivemos um erro para atualizar os dados da sua questão. Por favor, tente novamente mais tarde.",
            confirm = "Ok",
            cancel = null
        ) {
            showDefaultViewState()
        }
    }

    private fun showLoadingViewState() {
        group_loading_edit.visibility = View.VISIBLE
    }

    private fun showDefaultViewState() {
        group_loading_edit.visibility = View.GONE
    }

    private fun populateQuestionFields(currentQuestion: Question) {
        populateQuestionSummary(currentQuestion.summary)
        populateQuestionAnswers(currentQuestion.answers)
    }

    private fun populateQuestionAnswers(questionAnswers: List<Answer>) {
        questionAnswers.forEachIndexed { index, answer ->
            val correctAnswer = answer.correctAnswer
            when (index) {
                0 -> {
                    questionsHallViewModel.answerMap[editText_first_question_edit] = answer
                    editText_first_question_edit.setText(answer.summary)
                    first_option.isChecked = correctAnswer
                }

                1 -> {
                    questionsHallViewModel.answerMap[editText_second_question_edit] = answer
                    editText_second_question_edit.setText(answer.summary)
                    second_option.isChecked = correctAnswer
                }

                2 -> {
                    questionsHallViewModel.answerMap[editText_third_question_edit] = answer
                    editText_third_question_edit.setText(answer.summary)
                    third_option.isChecked = correctAnswer
                }

                3 -> {
                    questionsHallViewModel.answerMap[editText_forth_question_edit] = answer
                    editText_forth_question_edit.setText(answer.summary)
                    forth_option.isChecked = correctAnswer
                }

                4 -> {
                    questionsHallViewModel.answerMap[editText_fifth_question_edit] = answer
                    editText_fifth_question_edit.setText(answer.summary)
                    fifth_option.isChecked = correctAnswer
                }

                else -> {
                    //ignore
                }
            }
        }
    }

    private fun populateQuestionSummary(questionSummary: String) {
        textView_question_edit_summary_edit.setText(questionSummary)
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            onBackPressedRoutine()
        }

        materialButton_delete_question.setOnClickListener {
            showDeleteWarningDialog()
        }

        materialButton_update_question.setOnClickListener {
            validateQuestionForm()
        }
    }

    private fun showDeleteWarningDialog() {
        showDialog(
            title = "ATENÇÃO!",
            description = "Você esta tentando deletar esta pergunta no banco! Essa é uma ação irreversível e a pergunta será perdida para sempre! Deseja realmente continuar?",
            confirm = "Sim",
            cancel = "Não"
        ) {
            deleteQuestion()
        }
    }

    private fun deleteQuestion() {
        questionsHallViewModel.deleteCurrentQuestion()
    }

    private fun validateQuestionForm() {
        if (checkFields()) {
            if (validateFields()) {
                showConfirmUpdateDialog()
            } else {
                showDuplicateAlternativesDialog()
            }
        } else {
            showErrorCreationDialog()
        }
    }

    private fun showConfirmUpdateDialog() {
        showDialog(
            title = "Atenção!",
            description = "Você esta prestes a alterar algumas informações sobre esta pergunta, deseja realmente continuar?",
            confirm = "Sim",
            cancel = "Não"
        ) {
            createQuestion()
        }
    }

    private fun createQuestion() {
        questionsHallViewModel.questionSummary =
            textView_question_edit_summary_edit.text.toString().trim()

        updateCheckedList()
        updateSummaryList()

//        questionsHallViewModel.answersSummary[editText_first_question_edit.text.toString().trim()] =
//            first_option.isChecked
//        questionsHallViewModel.answersSummary[editText_second_question_edit.text.toString().trim()] =
//            second_option.isChecked
//        questionsHallViewModel.answersSummary[editText_third_question_edit.text.toString().trim()] =
//            third_option.isChecked
//        questionsHallViewModel.answersSummary[editText_forth_question_edit.text.toString().trim()] =
//            forth_option.isChecked
//        questionsHallViewModel.answersSummary[editText_fifth_question_edit.text.toString().trim()] =
//            fifth_option.isChecked

        questionsHallViewModel.updateQuestion()
    }

    private fun updateCheckedList() {
        questionsHallViewModel.answerMap[editText_first_question_edit]?.correctAnswer = first_option.isChecked
        questionsHallViewModel.answerMap[editText_second_question_edit]?.correctAnswer = second_option.isChecked
        questionsHallViewModel.answerMap[editText_third_question_edit]?.correctAnswer = third_option.isChecked
        questionsHallViewModel.answerMap[editText_forth_question_edit]?.correctAnswer = forth_option.isChecked
        questionsHallViewModel.answerMap[editText_fifth_question_edit]?.correctAnswer = fifth_option.isChecked
    }

    private fun updateSummaryList() {
        questionsHallViewModel.answerMap[editText_first_question_edit]?.summary = editText_first_question_edit.text.toString().trim()
        questionsHallViewModel.answerMap[editText_second_question_edit]?.summary = editText_second_question_edit.text.toString().trim()
        questionsHallViewModel.answerMap[editText_third_question_edit]?.summary = editText_third_question_edit.text.toString().trim()
        questionsHallViewModel.answerMap[editText_forth_question_edit]?.summary = editText_forth_question_edit.text.toString().trim()
        questionsHallViewModel.answerMap[editText_fifth_question_edit]?.summary = editText_fifth_question_edit.text.toString().trim()
    }

    private fun showErrorCreationDialog() {
        showDialog(
            title = "Oops...",
            description = "Aparentemente você não preencheu todos os campos para a questão. Certifique-se de que todos os campos estão preenchidos corretamante.",
            confirm = "Entendi",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun showDuplicateAlternativesDialog() {
        showDialog(
            title = "Atenção!",
            description = "Você não pode ter duas alternativas com a mesma resposta! Certifique-se de que cada alternativa possui sua resposta individual!",
            confirm = "Entendi",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun validateFields(): Boolean {
        val answersList = mutableListOf<String>()
        answersList.add(editText_first_question_edit.text.toString().trim())
        answersList.add(editText_second_question_edit.text.toString().trim())
        answersList.add(editText_third_question_edit.text.toString().trim())
        answersList.add(editText_forth_question_edit.text.toString().trim())
        answersList.add(editText_fifth_question_edit.text.toString().trim())
        return answersList.distinct().count() == answersList.count()
    }

    private fun checkFields(): Boolean {
        return textView_question_edit_summary_edit.text.toString().trim()
            .isNotBlank() || editText_first_question_edit.text.toString().trim().isNotBlank() ||
                editText_second_question_edit.text.toString().trim()
                    .isNotBlank() || editText_third_question_edit.text.toString().trim()
            .isNotBlank() ||
                editText_forth_question_edit.text.toString().trim()
                    .isNotBlank() || editText_fifth_question_edit.text.toString().trim()
            .isNotBlank()
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            onBackPressedRoutine()
        }
    }

    private fun onBackPressedRoutine() {
        questionsHallViewModel.clearCurrentSelectedQuestion()
        listener?.onBackToTeacherQuestionsList()
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
        fun onBackToTeacherQuestionsList()
    }

}