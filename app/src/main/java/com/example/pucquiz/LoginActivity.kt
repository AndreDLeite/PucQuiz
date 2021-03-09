package com.example.pucquiz

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.login.LoginFragment
import com.example.pucquiz.ui.register.RegisterFragment

class LoginActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onRegisterCompleted() {
        //TODO: Go to logged in fragment
    }

}