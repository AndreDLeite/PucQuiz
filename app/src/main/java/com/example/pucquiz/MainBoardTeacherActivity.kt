package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.mainboard.fragments.OnBoardingFragment
import com.example.pucquiz.ui.quizhall.fragments.QuestionCreationFragment
import com.example.pucquiz.ui.quizhall.fragments.QuestionEditFragment
import com.example.pucquiz.ui.quizhall.fragments.QuestionHallFragment
import com.example.pucquiz.ui.quizresults.framgents.QuestionResultFragment
import com.example.pucquiz.ui.quizresults.framgents.QuestionsResultGradeSelectionFragment
import com.example.pucquiz.ui.quizresults.framgents.QuestionsResultHallFragment
import com.example.pucquiz.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainBoardTeacherActivity : AppCompatActivity(),
    SettingsFragment.OnFragmentInteractionListener,
    QuestionHallFragment.OnFragmentInteractionListener,
    QuestionCreationFragment.OnFragmentInteractionListener,
    QuestionsResultGradeSelectionFragment.OnFragmentInteractionListener,
    QuestionsResultHallFragment.OnFragmentInteractionListener,
    QuestionResultFragment.OnFragmentInteractionListener,
    QuestionEditFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_teacher_onboarding)
        setupNavigation()
    }

    private fun setupNavigation() {
        val obBoardingFragment = OnBoardingFragment()
        val questionsFragment = QuestionHallFragment()
        val resultsFragment = QuestionsResultGradeSelectionFragment()
        val settingsFragment = SettingsFragment()
        makeCurrentFragment(obBoardingFragment)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.ic_create_quiz -> {
                    bottom_navigation.visibility = View.VISIBLE
                    makeCurrentFragment(questionsFragment)
                }

                R.id.ic_students_result -> {
                    bottom_navigation.visibility = View.VISIBLE
                    makeCurrentFragment(resultsFragment)
                }

                R.id.icon_teacher_setting -> {
                    bottom_navigation.visibility = View.VISIBLE
                    makeCurrentFragment(settingsFragment)
                }

                else -> {
                    //ignore: should not happen!
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
                replace(R.id.main_teacher_frame, fragment)
                commit()
            }
    }

    //region SettingsInteractions
    override fun onLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    //endregion

    //region QuestionsInteractions
    override fun onAddQuestionClicked() {
        bottom_navigation.visibility = View.GONE
        makeCurrentFragment(QuestionCreationFragment())
    }

    override fun onFragmentCreated() {
        bottom_navigation.visibility = View.VISIBLE
    }

    override fun backToQuestionsHall() {
        makeCurrentFragment(QuestionHallFragment(), false)
    }

    override fun onQuestionEditClicked() {
        bottom_navigation.visibility = View.GONE
        makeCurrentFragment(QuestionEditFragment())
    }
    //endregion

    //region TeacherQuestionsResult
    override fun onGradeCardClicked() {
        makeCurrentFragment(QuestionsResultHallFragment())
    }

    override fun onBackToTeacherGradeSelection() {
        makeCurrentFragment(QuestionsResultGradeSelectionFragment(), false)
    }

    override fun onQuestionCardClicked() {
        makeCurrentFragment(QuestionResultFragment())
    }

    override fun onBackToQuestionResultHall() {
        makeCurrentFragment(QuestionsResultHallFragment(), false)
    }

    override fun onBackToTeacherQuestionsList() {
        makeCurrentFragment(QuestionHallFragment(), false)
    }
    //endregion

}