package com.betan.betanstoktakip.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

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

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <VBinding : ViewBinding> ViewGroup.inflate(
    bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> VBinding,
): VBinding =
    bindingInflater.invoke(layoutInflater(), this, false)

fun View.layoutInflater(): LayoutInflater = LayoutInflater.from(context)