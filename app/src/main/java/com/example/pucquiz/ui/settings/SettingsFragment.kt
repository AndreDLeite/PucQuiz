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
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoVewModel
import com.example.pucquiz.ui.settings.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel by inject<SettingsViewModel>()
    private val userInfoViewModel by sharedViewModel<UserInfoVewModel>()
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
        overrideOnBackPressed()
        setupListeners()
        setupViewModelObservers()
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