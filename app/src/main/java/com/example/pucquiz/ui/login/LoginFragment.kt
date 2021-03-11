package com.example.pucquiz.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.login.viewmodels.LoginViewModel
import com.example.pucquiz.ui.register.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val loginViewModel by sharedViewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewModelObservers()
        checkRegistrationStatus()
    }

    private fun setupListeners() {
        textView_register.setOnClickListener {
            listener?.onRegisterClicked()
        }

        materialButton_login.setOnClickListener {
            loginUser()
        }
    }

    private fun setupViewModelObservers() {
        loginViewModel.loginLiveData.observe(viewLifecycleOwner, Observer { itLoginOperation ->
            when (itLoginOperation.status) {
                Resource.Status.SUCCESS -> {
                    listener?.onSuccessfulLogin()
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {
                    showLoadingViewState()
                }
            }
        })
    }

    private fun checkRegistrationStatus() {
        if(loginViewModel.getRegistrationValue()) {
            loginViewModel.setRegistrationValue(false)
            showRegistrationDialog()
        }
    }

    private fun loginUser() {
        if (validateUserData()) {
            val userEmail = editTextTextPersonEmail.text.toString()
            val userPassword = editTextTextPassword.text.toString()
            loginViewModel.loginUser(userEmail, userPassword)
        }
    }

    private fun showLoadingViewState() {
        progress_bar.visibility = View.VISIBLE
        editTextTextPersonEmail.visibility = View.GONE
        editTextTextPassword.visibility = View.GONE
        materialButton_login.isEnabled = false
        textView_forgot_password.isEnabled = false
        textView_register.isEnabled = false
    }

    private fun showRegistrationDialog() {
        val dialog = DialogSimple()

        dialog.setDialogParameters(
            title = "Seu cadastro foi feito com sucesso!",
            description = "Entre com seus dados registrados para começar a utilizar o App.",
            confirmText = "Ok",
            cancelText = null
        )

        dialog.setDialogListener(object : DialogSimple.SimpleDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
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

    private fun validateUserData(): Boolean {
        val validationData = checkRegisterFields()
        val result = validationData.first
        val errorFields = validationData.second
        return if (result) {
            setErrorMessages(errorFields)
            false
        } else {
            true
        }
    }

    private fun checkRegisterFields(): Pair<Boolean, List<EditText>> {
        val componentsList = mutableListOf<EditText>()

        if (editTextTextPersonEmail.text.toString().trim().isBlank()) {
            componentsList.add(editTextTextPersonEmail)
        }
        if (editTextTextPassword.text.toString().trim().isEmpty()) {
            componentsList.add(editTextTextPassword)
        }
        return if (componentsList.isNotEmpty()) {
            Pair(true, componentsList)
        } else {
            Pair(false, emptyList())
        }
    }

    private fun setErrorMessages(components: List<EditText>) {
        components.forEach { itComponent ->
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
        fun onRegisterClicked()
        fun onSuccessfulLogin()

    }

}
