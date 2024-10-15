package com.betan.betanstoktakip.core.extensions

import android.view.View

fun View.click(clickListener: View.OnClickListener) {
    var lastClickTime: Long = 0

    setOnClickListener {
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - lastClickTime < 500L) {
            return@setOnClickListener
        }
        clickListener.onClick(it)
        lastClickTime = clickedTime
    }
}