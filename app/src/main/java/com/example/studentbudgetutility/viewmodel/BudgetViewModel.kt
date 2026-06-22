package com.example.studentbudgetutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BudgetViewModel : ViewModel() {

    var monthlyBudget by mutableStateOf(1000.0)
        private set

    var selectedCurrency by mutableStateOf("SGD")
        private set

    var expenses by mutableStateOf(sampleExpenses())
        private set

    var cycleLengthDays by mutableStateOf(30)
        private set

    var cycleStartTimeMillis by mutableStateOf(System.currentTimeMillis())
        private set

    var cycleEndTimeMillis by mutableStateOf(
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30)
    )
        private set

    var settingsMessage by mutableStateOf("")
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

    val spent: Double
        get() = expenses.sumOf { it.amount }

    val remaining: Double
        get() = monthlyBudget - spent

    val safeDailySpend: Double
        get() = remaining / cycleLengthDays

    val budgetUsedProgress: Float
        get() = (spent / monthlyBudget).toFloat().coerceIn(0f, 1f)

    val daysRemaining: Int
        get() {
            val difference = cycleEndTimeMillis - System.currentTimeMillis()
            return TimeUnit.MILLISECONDS.toDays(difference).toInt().coerceAtLeast(0)
        }

    fun checkBudgetCycle() {
        if (System.currentTimeMillis() >= cycleEndTimeMillis) {
            expenses = emptyList()
            cycleStartTimeMillis = System.currentTimeMillis()
            cycleEndTimeMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(cycleLengthDays.toLong())
            settingsMessage = "New budget cycle started. Transactions were reset."
        }
    }

    fun startNewCycleNow() {
        expenses = emptyList()
        cycleStartTimeMillis = System.currentTimeMillis()
        cycleEndTimeMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(cycleLengthDays.toLong())
        settingsMessage = "New cycle started."
    }

    fun updateCycleLength(days: Int) {
        if (days > 0) {
            cycleLengthDays = days
            cycleEndTimeMillis = cycleStartTimeMillis + TimeUnit.DAYS.toMillis(days.toLong())
            settingsMessage = "Budget cycle length updated."
        }
    }

    fun updateCycleStartDate(daysFromToday: Int) {
        cycleStartTimeMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(daysFromToday.toLong())
        cycleEndTimeMillis = cycleStartTimeMillis + TimeUnit.DAYS.toMillis(cycleLengthDays.toLong())
        expenses = emptyList()
        settingsMessage = "Budget cycle start date updated. Transactions were reset."
    }

    fun formattedCycleStartDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(cycleStartTimeMillis))
    }

    fun formattedCycleEndDate(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(cycleEndTimeMillis))
    }

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

    fun resetConversionRates() {
        conversionRates = mapOf(
            "SGD" to 1.0,
            "USD" to 0.78,
            "AUD" to 1.20,
            "MYR" to 3.30,
            "CNY" to 5.60,
            "UZS" to 10300.0
        )
        settingsMessage = "Conversion rates reset."
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