package com.example.pucquiz.extensios

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

fun FragmentActivity.overrideOnBackPressed(lifeCycleOwner: LifecycleOwner, block: () -> Unit) {
    this.onBackPressedDispatcher.addCallback(
        lifeCycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                block()
            }
        }
    )
}