package com.example.studentbudgetutility.util

import com.example.studentbudgetutility.model.Expense

object BudgetCalculator {

    fun calculateSpent(expenses: List<Expense>): Double {
        return expenses.sumOf { it.amount }
    }

    fun calculateRemaining(monthlyBudget: Double, expenses: List<Expense>): Double {
        return monthlyBudget - calculateSpent(expenses)
    }

    fun calculateSafeDailySpend(remaining: Double, daysRemaining: Int): Double {
        return if (daysRemaining > 0) {
            remaining / daysRemaining
        } else {
            remaining
        }
    }

    fun calculateBudgetUsedProgress(monthlyBudget: Double, spent: Double): Float {
        return if (monthlyBudget > 0) {
            (spent / monthlyBudget).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }
    }

    fun calculateAverageExpense(expenses: List<Expense>): Double {
        return if (expenses.isNotEmpty()) {
            expenses.sumOf { it.amount } / expenses.size
        } else {
            0.0
        }
    }

    fun findHighestCategory(expenses: List<Expense>): String {
        return expenses
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .maxByOrNull { it.value }
            ?.key ?: "No spending yet"
    }
}