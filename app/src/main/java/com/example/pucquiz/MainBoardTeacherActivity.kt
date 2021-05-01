package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.mainboard.fragments.OnBoardingFragment
import com.example.pucquiz.ui.quizhall.fragments.QuestionHallFragment
import com.example.pucquiz.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainBoardTeacherActivity : AppCompatActivity(),
    SettingsFragment.OnFragmentInteractionListener,
    QuestionHallFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_teacher_onboarding)
        setupNavigation()
    }

    private fun setupNavigation() {
        val obBoardingFragment = OnBoardingFragment()
        val questionsFragment = QuestionHallFragment()
        val settingsFragment = SettingsFragment()
//        val medalsFragment = MedalsFragment()
        makeCurrentFragment(obBoardingFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.ic_create_quiz -> {
                    makeCurrentFragment(questionsFragment)
                }

                R.id.ic_students_result -> {
                    makeCurrentFragment(obBoardingFragment)
                }

                R.id.icon_teacher_setting -> {
                    makeCurrentFragment(settingsFragment)
                }

                else -> {

                }
            }
            false

        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_teacher_frame, fragment)
            commit()
        }
    }

    //region Settings_Interactions
    override fun onLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    //endregion

    //region Questions_Interactions
    override fun onAddQuestionClicked() {
//        makeCurrentFragment(QuestionVisualizerFragment())
    }
    //endregion

}