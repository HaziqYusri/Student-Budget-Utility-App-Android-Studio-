package com.example.studentbudgetutility.model

data class Expense(
    val category: String,
    val amount: Double,
    val timestamp: Long,
    val id: Long = System.nanoTime()
)