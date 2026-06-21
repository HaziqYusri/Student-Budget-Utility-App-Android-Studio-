package com.example.studentbudgetutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.model.Expense
import java.text.NumberFormat
import java.util.Locale

class BudgetViewModel : ViewModel() {

    var monthlyBudget by mutableStateOf(1000.0)
        private set

    var selectedCurrency by mutableStateOf("SGD")
        private set

    var expenses by mutableStateOf(sampleExpenses())
        private set

    var conversionRates by mutableStateOf(
        mapOf(
            "SGD" to 1.0,
            "USD" to 0.78,
            "AUD" to 1.20,
            "MYR" to 3.30,
            "CNY" to 5.60,
            "UZS" to 10300.0
        )
    )
        private set

    var settingsMessage by mutableStateOf("")
        private set

    val spent: Double
        get() = expenses.sumOf { it.amount }

    val remaining: Double
        get() = monthlyBudget - spent

    val safeDailySpend: Double
        get() = remaining / 30

    val budgetUsedProgress: Float
        get() = (spent / monthlyBudget).toFloat().coerceIn(0f, 1f)

    fun addExpense(expense: Expense) {
        expenses = expenses + expense
    }

    fun deleteExpense(expense: Expense) {
        expenses = expenses.filterNot { it.id == expense.id }
    }

    fun clearAllExpenses() {
        expenses = emptyList()
        settingsMessage = "All transactions cleared."
    }

    fun resetSampleExpenses() {
        expenses = sampleExpenses()
        settingsMessage = "Sample transactions restored."
    }

    fun updateMonthlyBudget(newBudget: Double) {
        if (newBudget > 0) {
            monthlyBudget = newBudget
            settingsMessage = "Budget updated."
        }
    }

    fun updateCurrency(currency: String) {
        selectedCurrency = currency
    }

    fun updateConversionRate(currency: String, newRate: Double) {
        if (newRate > 0) {
            conversionRates = conversionRates.toMutableMap().apply {
                this[currency] = newRate
            }
        }
    }

    fun convertAmount(amount: Double): Double {
        val rate = conversionRates[selectedCurrency] ?: 1.0
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

        return if (selectedCurrency == "UZS") {
            val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
            formatter.maximumFractionDigits = 0
            "${formatter.format(converted)} so'm"
        } else {
            val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
            formatter.minimumFractionDigits = 2
            formatter.maximumFractionDigits = 2
            "${currencySymbol()}${formatter.format(converted)}"
        }
    }
}