package com.betan.betanstoktakip.core.extensions

fun Double?.orZero(): Double {
    return this?.let { this } ?: 0.0
}

fun Any?.toDoubleOrZero(): Double {
    return toString().toDoubleOrNull() ?: 0.0
}