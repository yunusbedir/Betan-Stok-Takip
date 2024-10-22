package com.betan.betanstoktakip.core.extensions


const val DECIMAL_GROUP_SIZE = 3

fun String.removeDots(): String = this.replace(".", "")

fun String.putMoneyDots(): String { //for price field formats, 12.345 1.234.567 etc.
    val builder = java.lang.StringBuilder()
    for ((index, char) in this.withIndex()) {
        if ((this.length - index) % DECIMAL_GROUP_SIZE == 0 && index != 0) {
            builder.append(".")
        }
        builder.append(char)
    }
    return builder.toString()
}

fun Double.toMoney(currency: String = "TL"): String {
    return "${toIntOrZero().toString().putMoneyDots()} $currency"
}

fun String.fromMoneyToDouble(currency: String = "TL"): Double {
    return removeDots().replace(" $currency", "").toDoubleOrZero()
}