package com.betan.betanstoktakip.core.extensions

import android.graphics.Paint
import android.widget.TextView
import androidx.core.view.isVisible

fun TextView.strikeThroughText(showStrikeThrough: Boolean) {
    paintFlags = if (showStrikeThrough) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
    isVisible = showStrikeThrough
}