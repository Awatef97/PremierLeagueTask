package com.example.carefertask.core.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.getFormattedDate(): String {
    if (this.isBlank()) return ""
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(this)
    val formatter = SimpleDateFormat("EEE d MMM")
    return formatter.format(date)
}

fun String?.getDate(): Date? {
    if (this == null)
        return null
    val sdf = SimpleDateFormat("EEE d MMM", Locale("en"))
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this)
}