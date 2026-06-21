package com.example.studentbudgetutility.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.model.Expense

@Composable
fun SpendingInsightsCard(
    expenses: List<Expense>,
    formatMoney: (Double) -> String
) {
    val totalTransactions = expenses.size
    val averageExpense = if (expenses.isNotEmpty()) {
        expenses.sumOf { it.amount } / expenses.size
    } else {
        0.0
    }

    val highestCategory = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .maxByOrNull { it.value }
        ?.key ?: "No spending yet"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Spending Insights",
                style = MaterialTheme.typography.titleLarge
            )

            Text("Transactions: $totalTransactions")
            Text("Average expense: ${formatMoney(averageExpense)}")
            Text("Highest category: $highestCategory")
        }
    }
}