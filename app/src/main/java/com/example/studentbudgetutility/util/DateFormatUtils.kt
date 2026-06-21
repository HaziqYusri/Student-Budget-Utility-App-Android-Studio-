package com.example.studentbudgetutility.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}