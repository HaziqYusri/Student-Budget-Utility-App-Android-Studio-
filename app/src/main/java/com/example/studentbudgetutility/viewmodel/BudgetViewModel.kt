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
        if (newBudget > 0) {
            monthlyBudget = newBudget
        }
    }
}