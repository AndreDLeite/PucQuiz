package com.example.pucquiz.extensios

import android.view.View

fun View.fadInAnimation(duration: Long = 2000, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}