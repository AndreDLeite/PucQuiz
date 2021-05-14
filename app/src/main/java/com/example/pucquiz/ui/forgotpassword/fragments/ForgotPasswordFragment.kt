package com.example.pucquiz.ui.forgotpassword.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.extensios.hideKeyboard
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.forgotpassword.viewmodels.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.backButton
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : Fragment() {

    private val forgotPasswordViewModel by inject<ForgotPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewModelObservers()
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        materialButton_reset_password.setOnClickListener {
            activity?.hideKeyboard(materialButton_reset_password)
            showLoadingViewState()
            validateEmailField()
        }
    }

    private fun setupViewModelObservers() {
        forgotPasswordViewModel.operationStatus.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    showSuccessDialog()
                }
                Resource.Status.ERROR -> {
                    itResource.message?.let {
                        showDefaultViewState()
                        showErrorDialog(it)
                    }
                }
                Resource.Status.LOADING -> {
                    showLoadingViewState()
                }
            }
        })
    }

    private fun showSuccessDialog() {
        showDialog(
            title = "Tudo certo!",
            description = "Enviamos um email para recuperação de sua sua no email descrito! Cheque sua caixa e siga o passo a passo.",
            confirm = "Entendi",
            cancel = null
        ) {
            forgotPasswordViewModel.clearViewModel()
            activity?.onBackPressed()
        }
    }

    private fun showErrorDialog(message: String) {
        showDialog(
            title = "Oops...",
            description = message,
            confirm = "Ok",
            cancel = null
        ) {
            //ignore
        }
    }

    private fun showLoadingViewState() {
        group_loading.visibility = View.VISIBLE
    }

    private fun validateEmailField() {
        val emailField = editText_user_forgot_email.text.toString().trim()
        if (emailField.isEmpty()) {
            editText_user_forgot_email.error = "Este campo não pode ser vazio."
            Handler().postDelayed({ showDefaultViewState() }, 300)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()) {
            editText_user_forgot_email.error = "Formato do e-mail invalido."
            Handler().postDelayed({ showDefaultViewState() }, 300)
            return
        }

        forgotPasswordViewModel.sendResetPassword(emailField)

    }

    private fun showDefaultViewState() {
        group_loading.visibility = View.GONE
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
}