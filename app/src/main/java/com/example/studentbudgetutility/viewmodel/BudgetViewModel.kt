package com.example.studentbudgetutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.model.Expense

class BudgetViewModel : ViewModel() {

    var monthlyBudget by mutableStateOf(1000.0)
        private set

    var selectedCurrency by mutableStateOf("USD")
        private set

    var expenses by mutableStateOf(sampleExpenses())
        private set

    val spent: Double
        get() = expenses.sumOf { it.amount }

    val remaining: Double
        get() = monthlyBudget - spent

    val safeDailySpend: Double
        get() = remaining / 30

    fun addExpense(expense: Expense) {
        expenses = expenses + expense
    }

    fun updateMonthlyBudget(newBudget: Double) {
        if (newBudget > 0) monthlyBudget = newBudget
    }

    fun updateCurrency(currency: String) {
        selectedCurrency = currency
    }

    fun convertAmount(amount: Double): Double {
        val rate = when (selectedCurrency) {
            "USD" -> 1.0
            "AUD" -> 1.53
            "MYR" -> 4.70
            "CNY" -> 7.25
            "UZS" -> 12650.0
            else -> 1.0
        }
        return amount * rate
    }

    fun currencySymbol(): String {
        return when (selectedCurrency) {
            "USD" -> "$"
            "AUD" -> "A$"
            "MYR" -> "RM"
            "CNY" -> "¥"
            "UZS" -> "so'm "
            else -> "$"
        }
    }

    fun formatMoney(amount: Double): String {
        return "${currencySymbol()}${"%.2f".format(convertAmount(amount))}"
    }
}