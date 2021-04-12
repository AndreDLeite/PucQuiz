package com.example.pucquiz.ui.mainboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pucquiz.R
import com.example.pucquiz.extensios.fadeInAnimation
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.models.User
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.mainboard.viewmodels.UserInfoVewModel
import kotlinx.android.synthetic.main.fragment_onboarding.*
import org.koin.android.ext.android.inject

class OnBoardingFragment : Fragment() {

    private val onBoardingViewModel by inject<UserInfoVewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupViewModelObservers()
        setupListeners()
    }

    private fun setupViewModelObservers() {
        onBoardingViewModel.currentLoggedUser.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    setGreetingsAnimations(it.data)
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {

                }
            }

        })
    }

    private fun setupListeners() {

    }

    private fun setGreetingsAnimations(user: User?) {
        user?.let { itUser ->
            textView_greetings.text = String.format(
                getString(R.string.onboarding_greetings),
                itUser.name
            )
            textView_greetings.fadeInAnimation(2000)
        }

        with(textView_instructions) {
            this.fadeInAnimation(5000)
        }
        with(imageView_warning) {
            this.fadeInAnimation(5000)
        }

    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }
}