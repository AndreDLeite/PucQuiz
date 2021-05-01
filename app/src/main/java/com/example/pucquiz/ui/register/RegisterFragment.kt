package com.example.pucquiz.ui.register

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.hideKeyboard
import com.example.pucquiz.models.UserRole
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.register.adapters.GradeSelectedListAdapter
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Math.abs

class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val registerViewModel by sharedViewModel<RegisterViewModel>()
    private val loginViewModel by sharedViewModel<LoginViewModel>()
    private var listener: OnFragmentInteractionListener? = null
    private var lastRecyclerViewDownTouchEvent: MotionEvent? = null
    private var gradeListAdapter = GradeSelectedListAdapter(emptyList())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO: Add 'eye' to show password
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupDropDownUserPeriod()
        initRecyclerView()
        setupViewModelObserver()
    }

    private fun setupListeners() {
        when(registerViewModel.currentUserRole) {
            UserRole.STUDENT -> {
                radioGroup.check(radioButton_student.id)
            }
            UserRole.TEACHER -> {
                radioGroup.check(radioButton_teacher.id)
            }
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                radioButton_student.id -> {
                    registerViewModel.currentUserRole = UserRole.STUDENT
                    registerViewModel.userPeriod.value?.let {
                        generateGrades(it)
                    }
                    resetSelectedGrades()
                }
                radioButton_teacher.id -> {
                    registerViewModel.currentUserRole = UserRole.TEACHER
                    generateGrades(5)
                }
            }

        }
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        materialButton_register.setOnClickListener {
            checkUserData()
            context?.hideKeyboard(materialButton_register)
        }
        textView_gradesHint.setOnClickListener {
            validateUserRole()
        }
    }

//    private fun showStudentRegisterForm() {
//        textView_register_grade_status.visibility = View.VISIBLE
//        spinner_drop_down_grade_selector.visibility = View.VISIBLE
//    }
//
//    private fun showTeacherRegisterForm() {
//        textView_register_grade_status.visibility = View.GONE
//        spinner_drop_down_grade_selector.visibility = View.GONE
//    }

    private fun validateUserRole() {
        openGradeSelector()
    }

    private fun initRecyclerView() {
        gradeListAdapter.setAnnotationTagAdapterListener(object :
            GradeSelectedListAdapter.OnInteractionClickListener {
            override fun onRecyclerViewItemClicked() {
                validateUserRole()
            }
        })

        with(recyclerView_selectedGrades) {
            adapter = gradeListAdapter
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)

            setOnTouchListener { _, event ->
                if (event?.action == MotionEvent.ACTION_DOWN && findChildViewUnder(
                        event.x,
                        event.y
                    ) == null
                ) {
                    lastRecyclerViewDownTouchEvent = event
                } else if (event?.action == MotionEvent.ACTION_UP && findChildViewUnder(
                        event.x,
                        event.y
                    ) == null
                    && lastRecyclerViewDownTouchEvent != null
                ) {
                    lastRecyclerViewDownTouchEvent?.let {
                        // Check to see if it was a tap or a swipe
                        val yDelta = abs(it.y - event.y)
                        if (yDelta > 30) {
                            validateUserRole()
                        }
                    }

                    lastRecyclerViewDownTouchEvent = null
                }

                false
            }
        }
    }

    private fun openGradeSelector() {
        listener?.onGradeSelectorClicked()
    }

    private fun setupDropDownUserPeriod() {
        context?.let { itContext ->
            val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                itContext,
                R.array.grade_period,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(R.layout.spinner_grade_item)
            spinner_drop_down_grade_selector.adapter = adapter
            spinner_drop_down_grade_selector.onItemSelectedListener = this
        }
    }

    private fun checkUserData() {
        if (checkRegisterFields()) {
            val username = editTextTextPersonName.text.toString().trim()
            val userAge = editTextTextPersonAge.text.toString().toInt()
            val userEmail = editTextTextEmail.text.toString().trim()
            val userPassword = editTextTextPassword.text.toString().trim()
            registerViewModel.registerUser(username, userAge, userEmail, userPassword)
        } else {
            showRegistrationErrorDialog()
        }
    }

    private fun setupViewModelObserver() {
        registerViewModel.registrationLiveData.observe(
            viewLifecycleOwner,
            Observer { itRegisterOperation ->
                itRegisterOperation ?: return@Observer
                when (itRegisterOperation.status) {
                    Resource.Status.SUCCESS -> {
                        showSuccessState()
                    }
                    Resource.Status.ERROR -> {
                        showRegistrationErrorDialog()
                    }
                    Resource.Status.LOADING -> {
                        showRegisteringOperationViewState()
                    }
                }
            })

        registerViewModel.selectedGrades
            .observe(viewLifecycleOwner, Observer { selectedGrades ->
                selectedGrades ?: return@Observer

                if (selectedGrades.isNotEmpty()) {
                    textView_gradesHint.visibility = View.INVISIBLE
                } else {
                    textView_gradesHint.visibility = View.VISIBLE
                }

                gradeListAdapter.replaceTags(selectedGrades)
            })
    }

    private fun showDefaultViewState() {
        progress_bar.visibility = View.GONE
        group_register_form.visibility = View.VISIBLE
        backButton.isEnabled = true
        materialButton_register.isEnabled = true
        textView_register_grade_ocupation.visibility = View.VISIBLE
        radioGroup.visibility = View.VISIBLE
    }

    private fun showRegistrationErrorDialog() {
        val dialog = DialogSimple()

        dialog.setDialogParameters(
            title = "Erro ao cadastrar",
            description = "Tivemos um problema com o seu registro. Por favor, verifique se seus dados estão corretos e válidos.",
            confirmText = "Ok",
            cancelText = null
        )

        dialog.setDialogListener(object : DialogSimple.SimpleDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                showDefaultViewState()
                registerViewModel.resetRegistrationOperation()
                dialog.dismiss()
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
                dialog.dismiss()
            }
        })

        activity?.run {
            dialog.show(supportFragmentManager, DialogSimple::class.java.simpleName)
        }
    }

    private fun showSuccessState() {
        loginViewModel.setRegistrationValue(true)
        registerViewModel.clearViewModel()
        listener?.onRegisterCompleted()
    }

    private fun showRegisteringOperationViewState() {
        group_register_form.visibility = View.GONE
        backButton.isEnabled = false
        materialButton_register.isEnabled = false
        progress_bar.visibility = View.VISIBLE
        textView_register_grade_ocupation.visibility = View.GONE
        radioGroup.visibility = View.GONE
    }

    private fun checkRegisterFields(): Boolean {
        var result = true

        if (editTextTextPersonName.text.toString().trim().isBlank()) {
            setComponentErrorMessage(editTextTextPersonName, "Este campo é obrigatório!")
            result = false
        }
        if (editTextTextPersonAge.text.toString().trim().isEmpty()) {
            setComponentErrorMessage(editTextTextPersonAge, "Este campo é obrigatório!")
            result = false

        }
        if (editTextTextEmail.text.toString().trim().isEmpty() ||
            !Patterns.EMAIL_ADDRESS.matcher(
                editTextTextEmail.text.toString().trim()
            ).matches()
        ) {
            setComponentErrorMessage(editTextTextEmail, "Este campo é obrigatório!")
            result = false

        }
        if (editTextTextPassword.text.toString().trim()
                .isEmpty() || editTextTextPassword.text.toString().trim().length < 6
        ) {
            setComponentErrorMessage(
                editTextTextPassword,
                "Senha invalida ou muito curta (menor que 6 caractéres)!"
            )
            result = false
        }
        if (registerViewModel.selectedGrades.value.isNullOrEmpty()) {
            textView_gradesHint.hint = "Voce deve selecionar ao menos uma matéria!"
            result = false
        }
        return result
    }

    private fun setComponentErrorMessage(component: EditText, message: String) {
        component.error = message
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
        val currentPeriod = adapterView?.getItemAtPosition(position).toString().toInt()
        if (currentPeriod != registerViewModel.userPeriod.value) {
            registerViewModel.setUserPeriod(currentPeriod)
            generateGrades(currentPeriod)
            resetSelectedGrades()
        }
    }

    private fun generateGrades(currentPeriod: Int) {
        registerViewModel.generateGradesBasedOnPeriod(currentPeriod)
    }

    private fun resetSelectedGrades() {
        registerViewModel.resetSelectedList()
        gradeListAdapter.replaceTags(emptyList())
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    interface OnFragmentInteractionListener {
        fun onRegisterCompleted()
        fun onGradeSelectorClicked()
    }
}