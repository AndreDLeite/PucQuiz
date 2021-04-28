package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.configuration.fragments.ConfigurationFragment

class ConfigurationActivity : AppCompatActivity(), ConfigurationFragment.ConfigurationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_configuration)

        replaceFragment(ConfigurationFragment(), false)

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

        fragmentTransaction.replace(R.id.frame_configuration, fragment).commit()
    }

    override fun onStudentLogin() {
        Handler().postDelayed({
            val intent = Intent(this, MainBoardStudentActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    override fun onTeacherLogin() {
        Handler().postDelayed({
            val intent = Intent(this, MainBoardTeacherActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

}