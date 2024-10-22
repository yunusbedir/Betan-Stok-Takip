package com.betan.betanstoktakip.core.extensions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FORMAT_STANDARD_UI = "dd/MM/yyyy HH:mm"

fun Timestamp.toStandardUiFormat(): String {
    return toDate().toStandardUiFormat()
}

fun Date.toStandardUiFormat(): String {
    return toFormat()
}

fun Timestamp.toFormat(format: String = FORMAT_STANDARD_UI): String {
    return toDate().toFormat(format)
}

fun Date.toFormat(format: String = FORMAT_STANDARD_UI): String {
    return try {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.format(time)
    } catch (e: Exception) {
        e.message.orEmpty()
    }
}