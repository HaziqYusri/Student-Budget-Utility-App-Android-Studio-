package com.example.studentbudgetutility.data

import com.example.studentbudgetutility.model.Expense

fun sampleExpenses(): List<Expense> {
    val now = System.currentTimeMillis()

    return listOf(
        Expense("Food", 15.0, now - 1000 * 60 * 10),
        Expense("Transport", 8.0, now - 1000 * 60 * 30),
        Expense("Coffee", 6.0, now - 1000 * 60 * 60)
    )
}