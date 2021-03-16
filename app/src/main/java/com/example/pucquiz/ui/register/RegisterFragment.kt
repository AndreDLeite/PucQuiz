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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.hideKeyboard
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
        setupDropDownGradeStatus()
        initRecyclerView()
        setupViewModelObserver()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        materialButton_register.setOnClickListener {
            checkUserData()
            context?.hideKeyboard(materialButton_register)
        }
        textView_gradesHint.setOnClickListener {
            openGradeSelector()
        }
    }

    private fun initRecyclerView() {
        gradeListAdapter.setAnnotationTagAdapterListener(object :
            GradeSelectedListAdapter.OnInteractionClickListener {
            override fun onRecyclerViewItemClicked() {
                openGradeSelector()
            }
        })

        with(recyclerView_selectedGrades) {
            adapter = gradeListAdapter
            layoutManager = GridLayoutManager(activity, 2)
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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
                            openGradeSelector()
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

    private fun setupDropDownGradeStatus() {
        context?.let { itContext ->
            val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                itContext,
                R.array.grade_status,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
        }
    }

    private fun setupViewModelObserver() {
        registerViewModel.registrationLiveData.observe(
            viewLifecycleOwner,
            Observer { itRegisterOperation ->
                when (itRegisterOperation.status) {
                    Resource.Status.SUCCESS -> {
                        showSuccessState()
                    }
                    Resource.Status.ERROR -> {
                        showRegistrationDialog()
                    }
                    Resource.Status.LOADING -> {
                        showRegisteringOperationViewState()
                    }
                }
            })

        registerViewModel.selectedGrades
            .observe(viewLifecycleOwner, Observer { selectedTagsList ->
                selectedTagsList ?: return@Observer

                if (selectedTagsList.isNotEmpty()) {
                    textView_gradesHint.visibility = View.INVISIBLE
                } else {
                    textView_gradesHint.visibility = View.VISIBLE
                }

                gradeListAdapter.replaceTags(selectedTagsList)
            })
    }

    private fun showDefaultViewState() {
        progress_bar.visibility = View.GONE
        group_register_form.visibility = View.VISIBLE
        backButton.isEnabled = true
        materialButton_register.isEnabled = true
    }

    private fun showRegistrationDialog() {
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
        listener?.onRegisterCompleted()
    }

    private fun showRegisteringOperationViewState() {
        group_register_form.visibility = View.GONE
        backButton.isEnabled = false
        materialButton_register.isEnabled = false
        progress_bar.visibility = View.VISIBLE
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
                "Senha invalida ou muito curta (menor que 6 digitos)!"
            )
            result = false
        }
        return result
    }

    private fun setComponentErrorMessage(component: EditText, message: String) {
        component.error = message
    }

    private fun setErrorMessages(components: List<EditText>) {
        components.forEach { itComponent ->
            //TODO: Add validation for password to me at least 6 length
            itComponent.error = "Este campo é obrigatório"
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
        registerViewModel.userGradeStatus = adapterView?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    interface OnFragmentInteractionListener {
        fun onRegisterCompleted()
        fun onGradeSelectorClicked()
    }
}