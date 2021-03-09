package com.example.pucquiz.ui.register

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.extensios.hideKeyboard
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val registerViewModel by inject<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        if (validateUserData()) {
            val username = editTextTextPersonName.text.toString().trim()
            val userAge = editTextTextPersonAge.text.toString().toInt()
            val userEmail = editTextTextEmail.text.toString().trim()
            val userPassword = editTextTextPassword.text.toString().trim()
            registerViewModel.registerUser(username, userAge, userEmail, userPassword)
        }
    }

    private fun setupViewModelObserver() {
        registerViewModel.registrationLiveData.observe(viewLifecycleOwner, Observer { itOperation ->
            when(itOperation.status) {
                Resource.Status.SUCCESS -> {
                    showSuccessState()
                }
                Resource.Status.ERROR -> {
                    //TODO: Add error layout
                }
                Resource.Status.LOADING -> {
                    showRegisteringOperationViewState()
                }
            }
        })
    }

    private fun showSuccessState() {
        listener?.onRegisterCompleted()
    }

    private fun showRegisteringOperationViewState() {
        group_register_form.visibility = View.GONE
        backButton.isEnabled = false
        materialButton_register.isEnabled = false
        progress_bar.visibility = View.VISIBLE
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

        if (editTextTextPersonName.text.toString().trim().isBlank()) {
            componentsList.add(editTextTextPersonName)
        }
        if (editTextTextPersonAge.text.toString().trim().isEmpty()) {
            componentsList.add(editTextTextPersonAge)
        }
        if (editTextTextEmail.text.toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                editTextTextEmail.text.toString().trim()
            ).matches()
        ) {
            componentsList.add(editTextTextEmail)
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
        fun onRegisterCompleted()
    }

}