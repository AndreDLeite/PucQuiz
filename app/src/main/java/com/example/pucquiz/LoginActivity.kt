package com.example.pucquiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.login.LoginFragment
import com.example.pucquiz.ui.register.GradeSelectionFragment
import com.example.pucquiz.ui.register.RegisterFragment

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_login)
        replaceFragment(LoginFragment(), false)

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

        fragmentTransaction.replace(R.id.frame_login, fragment).commit()
    }

    override fun onRegisterClicked() {
        replaceFragment(RegisterFragment(), true)
    }

    override fun onSuccessfullLogin() {
        val intent = Intent(this, MainBoardActivity::class.java)
        startActivity(intent)
    }

    override fun onRegisterCompleted() {
        replaceFragment(LoginFragment(), false)
    }

    override fun onGradeSelectorClicked() {
        replaceFragment(GradeSelectionFragment(), true)
    }

}