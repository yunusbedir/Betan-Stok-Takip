package com.betan.betanstoktakip.core.extensions

fun Int?.orZero(): Int {
    return this?.let { this } ?: 0
}

fun Any?.toIntOrZero(): Int {
    return toString().toIntOrNull() ?: 0
}