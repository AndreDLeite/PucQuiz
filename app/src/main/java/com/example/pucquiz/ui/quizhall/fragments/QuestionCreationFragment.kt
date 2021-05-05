package com.example.pucquiz.ui.quizhall.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.Grade
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionCreationViewModel
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionsHallViewModel
import kotlinx.android.synthetic.main.fragment_question_creation.*
import kotlinx.android.synthetic.main.fragment_question_creation.backButton
import kotlinx.android.synthetic.main.fragment_question_creation.spinner_drop_down_grade_selector
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuestionCreationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var listener: OnFragmentInteractionListener? = null

    private val userInfoView by sharedViewModel<UserInfoViewModel>()
    private val questionViewModel by sharedViewModel<QuestionCreationViewModel>()
    private val questionsHallViewModel by sharedViewModel<QuestionsHallViewModel>()

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
        fetchData()
        overrideOnBackPressed()
        setupListeners()
        setupRadioGroup()
        setupViewModelObservers()
    }

    private fun fetchData() {
        userInfoView.fetchUserData()
    }

    private fun setupViewModelObservers() {
        userInfoView.currentUserInfo.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        setupDropDownTeacherGrades(it.registered_grades)
                    }
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {
                    //ignore
                }
            }
        })

        questionViewModel.questionRegistrationLiveData.observe(
            viewLifecycleOwner,
            Observer { itResource ->
                itResource ?: return@Observer
                when (itResource.status) {
                    Resource.Status.SUCCESS -> {
                        questionsHallViewModel.setRegistrationValue(true)
                        Handler().postDelayed({ onQuestionCreated() }, 1000)
                    }
                    Resource.Status.ERROR -> {
                        showDefaultState()
                        showErrorCreationDialog()
                    }
                    Resource.Status.LOADING -> {
                        showLoadingViewState()
                    }
                }
            })
    }

    private fun onQuestionCreated() {
        questionViewModel.clearViewModel()
        listener?.backToQuestionsHall()
    }

    private fun showDefaultState() {
        backButton.isEnabled = true
        progress_bar_question_creaton.visibility = View.GONE
        group_register_question_form.visibility = View.VISIBLE
    }

    private fun showLoadingViewState() {
        backButton.isEnabled = false
        progress_bar_question_creaton.visibility = View.VISIBLE
        group_register_question_form.visibility = View.GONE
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            validateOnBackPressed()
        }

        materialButton_register_question.setOnClickListener {
            showQuestionCreationDialog()
        }

        imageView_summary_hint.setOnClickListener {
            showSummaryHintDialog()
        }

        imageView_alternatives_hint.setOnClickListener {
            showAlternativesHintDialog()
        }

        imageView_grade_hint.setOnClickListener {
            showGradeHintDialog()
        }
    }

    private fun showQuestionCreationDialog() {
        showDialog(
            title = "Atenção!",
            description = "Você está prestes a criar uma questão, os campos preenchidos não poderão ser alterados no futuro. Tem certeza de que deseja continuar?",
            confirm = "Sim",
            cancel = "Não"
        ) {
            validateQuestionForm()
        }
    }

    private fun setupDropDownTeacherGrades(grades: List<Grade>) {
        val gradeList = grades.map { it.gradeType }
        context?.let { itContext ->
            val adapter = ArrayAdapter<String>(
                itContext,
                android.R.layout.simple_spinner_item,
                gradeList
            )
            adapter.setDropDownViewResource(R.layout.spinner_grade_item)
            spinner_drop_down_grade_selector.adapter = adapter
            spinner_drop_down_grade_selector.onItemSelectedListener = this
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

    private fun showGradeHintDialog() {
        showDialog(
            title = "Matéria",
            description = "Aqui você escolhe para qual matéria esta pergunta será disponibilizada. A list se baseia nas matérias escolhidas ao realizar o cadastro.",
            confirm = "Entendi",
            cancel = null
        ) {
            //ignore
        }
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
        } else {
            showErrorCreationDialog()
        }
    }

    private fun createQuestion() {
        questionViewModel.questionSummary = editText_question_summary.text.toString().trim()
        questionViewModel.answers[editText_first_question.text.toString().trim()] =
            first_option.isChecked
        questionViewModel.answers[editText_second_question.text.toString().trim()] =
            second_option.isChecked
        questionViewModel.answers[editText_third_question.text.toString().trim()] =
            third_option.isChecked
        questionViewModel.answers[editText_forth_question.text.toString().trim()] =
            forth_option.isChecked
        questionViewModel.answers[editText_fifth_question.text.toString().trim()] =
            fifth_option.isChecked

        questionViewModel.createQuestion()

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

    override fun onItemSelected(
        adapterView: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        questionViewModel.currentQuestionGrade = adapterView?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    interface OnFragmentInteractionListener {
        fun backToQuestionsHall()
    }
}
