package com.example.pucquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.onboarding.OnBoardingFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setupNavigation()

    }

    private fun setupNavigation() {
        val onboarding = OnBoardingFragment()

        makeCurrentFragment(onboarding)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.icon_results -> {
                    makeCurrentFragment(onboarding)
                }

                R.id.icon_setting -> {

                }

                R.id.ic_ranking -> {

                }

                else -> {

                }
            }
            true

        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, fragment)
            commit()
        }
    }

}