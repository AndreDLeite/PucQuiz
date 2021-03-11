package com.example.pucquiz.ui.register

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.hideKeyboard
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val registerViewModel by inject<RegisterViewModel>()
    private val loginViewModel by sharedViewModel<LoginViewModel>()

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
            description = "Tivemos um problema com o seu registro. Por favor, certifique-se que seus dados estão corretos e válidos.",
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

    interface OnFragmentInteractionListener {
        fun onRegisterCompleted()
    }
}