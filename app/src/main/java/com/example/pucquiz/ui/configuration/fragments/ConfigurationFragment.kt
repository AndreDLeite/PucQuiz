package com.example.pucquiz.ui.configuration.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.models.UserRole
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import org.koin.android.ext.android.inject

class ConfigurationFragment : Fragment() {

    private val userInfoViewModel by inject<UserInfoViewModel>()
    private var listener: ConfigurationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userInfoViewModel.fetchUserData()
        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        userInfoViewModel.currentUserInfo.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        validateUserRole(it.role)
                    }
                }
                Resource.Status.ERROR -> {
                    //TODO: Adicionar texto e botao pro cara poder votlar aqui
                }
                Resource.Status.LOADING -> {
                    // ignore
                }
            }
        })
    }

    private fun validateUserRole(userRole: UserRole) {
        when (userRole) {
            UserRole.STUDENT -> {
                listener?.onStudentLogin()
            }
            UserRole.TEACHER -> {
                listener?.onTeacherLogin()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ConfigurationListener) {
            listener = context
        }
    }

    interface ConfigurationListener {
        fun onStudentLogin()
        fun onTeacherLogin()
    }

}