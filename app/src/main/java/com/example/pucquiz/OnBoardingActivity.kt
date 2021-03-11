package com.example.pucquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
//        replaceFragment()
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.replace(R.id.frame_on_boarding, fragment).commit()
    }
}