package com.example.pucquiz.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.components.DialogSimple
import com.example.pucquiz.controllers.GradeController
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserRole
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoViewModel
import com.example.pucquiz.ui.settings.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel by inject<SettingsViewModel>()
    private val userInfoViewModel by sharedViewModel<UserInfoViewModel>()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchInitialData()
        overrideOnBackPressed()
        setupListeners()
        setupViewModelObservers()
    }

    private fun fetchInitialData() {
        userInfoViewModel.fetchUserData()
        userInfoViewModel.fetchUserAdditionalData()
    }

    private fun setupListeners() {
        materialButton_logout.setOnClickListener {
            showLoginErrorDialog()
        }
    }

    private fun setupViewModelObservers() {
        userInfoViewModel.currentUserInfo.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        setupUserInformationText(it)
                        setupGradeList(it)
                        validatePointsFields(it.role)
                    }
                }
                Resource.Status.ERROR -> {
                    setupLoadingUserInformationText()
                }
                Resource.Status.LOADING -> {
                    setupErrorUserInformationText()
                }
            }
        })

        userInfoViewModel.currentUserAdditionalInfo.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        setupUserScoreInformationText(it)
                    }
                }
                Resource.Status.ERROR -> {
                    textView_user_score.text = "Erro ao carregar informações do servidor..."
                }
                Resource.Status.LOADING -> {
                    textView_user_score.text = "Carregando suas informações."
                }
            }
        })
    }

    private fun validatePointsFields(currentUserRole: UserRole) {
        when(currentUserRole) {
            UserRole.STUDENT -> {
                textView_user_score_title.visibility = View.VISIBLE
                textView_user_score.visibility = View.VISIBLE
            }
            UserRole.TEACHER -> {
                textView_user_score_title.visibility = View.GONE
                textView_user_score.visibility = View.GONE
            }
        }
    }

    private fun setupGradeList(currentUser: User) {
        if(textView_user_grades.text.toString().trim().isBlank()) {
            currentUser.registered_grades.forEach {
                textView_user_grades.append("${it.gradeType} ")
            }
        }
    }

    private fun setupLoadingUserInformationText() {
        textView_user_name.text = "Carregando suas informações."
        textView_user_email.text = "Carregando suas informações."
    }

    private fun setupErrorUserInformationText() {
        textView_user_name.text = "Erro ao carregar informações do servidor..."
        textView_user_email.text = "Erro ao carregar informações do servidor..."
    }

    private fun setupUserInformationText(user: User) {
        textView_user_name.text = user.name
        textView_user_email.text = user.email
    }

    private fun setupUserScoreInformationText(userAdditionalInfo: UserAdditionalInfo) {
        textView_user_score.text = userAdditionalInfo.userScore.toString()
    }

    private fun logOutCurrentUser() {
        settingsViewModel.logOutCurrentUser()
        listener?.onLogout()
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Add onBackPressed action here
        }
    }

    private fun showLoginErrorDialog() {
        val dialog = DialogSimple()

        dialog.setDialogParameters(
            title = getString(R.string.all_atention),
            description = getString(R.string.logout_warning),
            confirmText = getString(R.string.all_yes),
            cancelText = getString(R.string.all_no)
        )

        dialog.setDialogListener(object : DialogSimple.SimpleDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                logOutCurrentUser()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    interface OnFragmentInteractionListener {
        fun onLogout()
    }

}