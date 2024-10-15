package com.betan.betanstoktakip.core.extensions

import android.text.Editable

fun Editable?.orEmpty(): String =
    this?.toString().orEmpty()