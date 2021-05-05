package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.mainboard.fragments.OnBoardingFragment
import com.example.pucquiz.ui.medals.fragments.MedalsFragment
import com.example.pucquiz.ui.quizselector.fragments.QuizConfigurationFragment
import com.example.pucquiz.ui.quizselector.fragments.QuizGradeSelectionFragment
import com.example.pucquiz.ui.quizselector.fragments.QuizSelectionFragment
import com.example.pucquiz.ui.quizselector.viewmodels.QuizConfigurationViewModel
import com.example.pucquiz.ui.ranking.fragments.RankingFragment
import com.example.pucquiz.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainBoardStudentActivity : AppCompatActivity(),
    SettingsFragment.OnFragmentInteractionListener,
    QuizSelectionFragment.OnFragmentInteractionListener,
    QuizGradeSelectionFragment.OnFragmentInteractionListener {

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
        val quizFragment = QuizSelectionFragment()

        makeCurrentFragment(obBoardingFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_quiz -> {
                    makeCurrentFragment(quizFragment)
                }

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

    private fun makeCurrentFragment(fragment: Fragment, ltr: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                if (ltr) R.anim.enter_from_right else R.anim.enter_from_left,
                if (ltr) R.anim.exit_to_left else R.anim.exit_to_right,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            ).addToBackStack(null)
            .apply {
                replace(R.id.main_frame, fragment)
                commit()
            }
    }

    override fun onLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onQuizTypeClicked() {
        makeCurrentFragment(QuizGradeSelectionFragment())
    }

    override fun onQuizSetup() {
        makeCurrentFragment(QuizConfigurationFragment())
    }

}