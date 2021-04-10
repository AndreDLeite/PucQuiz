package com.example.pucquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.mainboard.fragments.OnBoardingFragment
import com.example.pucquiz.ui.mainboard.fragments.RankingFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setupNavigation()

    }

    private fun setupNavigation() {
        val obBoardingFragment = OnBoardingFragment()
        val rankingFragment = RankingFragment()

        makeCurrentFragment(obBoardingFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.icon_results -> {
                    makeCurrentFragment(obBoardingFragment)
                }

                R.id.icon_setting -> {

                }

                R.id.ic_ranking -> {
                    makeCurrentFragment(rankingFragment)
                }

                else -> {

                }
            }
            false

        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, fragment)
            commit()
        }
    }

}