package com.example.studentbudgetutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.model.Expense
import java.text.NumberFormat

class BudgetViewModel : ViewModel() {

    var monthlyBudget by mutableStateOf(1000.0)
        private set

    var selectedCurrency by mutableStateOf("SGD")
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

    fun deleteExpense(expense: Expense) {
        expenses = expenses.filterNot { it.id == expense.id }
    }

    fun updateMonthlyBudget(newBudget: Double) {
        if (newBudget > 0) monthlyBudget = newBudget
    }

    fun updateCurrency(currency: String) {
        selectedCurrency = currency
    }

    fun convertAmount(amount: Double): Double {
        val rate = when (selectedCurrency) {
            "SGD" -> 1.0
            "USD" -> 0.78
            "AUD" -> 1.20
            "MYR" -> 3.30
            "CNY" -> 5.60
            "UZS" -> 10300.0
            else -> 1.0
        }
        return amount * rate
    }

    fun currencySymbol(): String {
        return when (selectedCurrency) {
            "SGD" -> "S$"
            "USD" -> "$"
            "AUD" -> "A$"
            "MYR" -> "RM"
            "CNY" -> "¥"
            "UZS" -> ""
            else -> "S$"
        }
    }

    fun formatMoney(amount: Double): String {
        val converted = convertAmount(amount)
        val formatter = NumberFormat.getNumberInstance()

        return when (selectedCurrency) {
            "UZS" -> "${formatter.format(converted)} so'm"
            else -> "${currencySymbol()}${formatter.format(converted)}"
        }
    }
}