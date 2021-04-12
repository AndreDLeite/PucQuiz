package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.splash.SplashViewModel
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by inject<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        checkLoggedInUser()

    }

    private fun checkLoggedInUser() {
        splashViewModel.loggedUserLiveDataResult.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    it.data?.let {
                        goto(it)
                    }
                }
                else -> {
                    //ignore
                }
            }
        })
    }

    private fun goto(result: Boolean) {
        if (result) {
            gotoOnBoarding()
        } else {
            gotoLogin()
        }
    }

    private fun gotoLogin() {
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun gotoOnBoarding() {
        Handler().postDelayed({
            val intent = Intent(this, MainBoardActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

}