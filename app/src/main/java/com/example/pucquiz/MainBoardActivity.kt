package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.mainboard.fragments.OnBoardingFragment
import com.example.pucquiz.ui.medals.fragments.MedalsFragment
import com.example.pucquiz.ui.ranking.fragments.RankingFragment
import com.example.pucquiz.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainBoardActivity : AppCompatActivity(), SettingsFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setupNavigation()

    }

    private fun setupNavigation() {
        val obBoardingFragment = OnBoardingFragment()
        val rankingFragment = RankingFragment()
        val settingsFragment = SettingsFragment()
        val medalsFragment = MedalsFragment()

        makeCurrentFragment(obBoardingFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.icon_results -> {
//                    makeCurrentFragment(obBoardingFragment)
//                }

                R.id.icon_setting -> {
                    makeCurrentFragment(settingsFragment)
                }

                R.id.ic_ranking -> {
                    makeCurrentFragment(rankingFragment)
                }

                R.id.ic_medalhas -> {
                    makeCurrentFragment(medalsFragment)
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

    override fun onLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}