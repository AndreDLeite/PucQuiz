package com.example.pucquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.pucquiz.ui.redes.RedesQuizFragment
import com.example.pucquiz.ui.welcome.WelcomeFragment

class MainActivity : AppCompatActivity(), WelcomeFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(WelcomeFragment(), false)

    }

    private fun replaceFragment(@NonNull fragment: Fragment, @NonNull addToBackStack: Boolean) {
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

        fragmentTransaction.replace(R.id.frame_welcome, fragment).commit()
    }

    override fun onRedesQuizSelected() {
        replaceFragment(RedesQuizFragment(), true)
    }

    override fun onDistribuidasQuizSelected() {
//        replaceFragment(DistribuidasQuizFragment(), true)
    }

    override fun onHibridasQuizSelected() {
//        replaceFragment(HibridasQuizFragment(), true)
    }

}
