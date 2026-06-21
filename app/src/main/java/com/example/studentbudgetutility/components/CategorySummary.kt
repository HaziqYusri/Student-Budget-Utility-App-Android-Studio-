package com.example.studentbudgetutility.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.model.Expense

@Composable
fun CategorySummary(
    expenses: List<Expense>,
    formatMoney: (Double) -> String
) {
    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    val totalSpent = expenses.sumOf { it.amount }.coerceAtLeast(1.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Category Summary",
                style = MaterialTheme.typography.titleLarge
            )

            CategoryProgressRow("Food", categoryTotals["Food"] ?: 0.0, totalSpent, formatMoney)
            CategoryProgressRow("Transport", categoryTotals["Transport"] ?: 0.0, totalSpent, formatMoney)
            CategoryProgressRow("Entertainment", categoryTotals["Entertainment"] ?: 0.0, totalSpent, formatMoney)
            CategoryProgressRow("Shopping", categoryTotals["Shopping"] ?: 0.0, totalSpent, formatMoney)
        }
    }
}

@Composable
fun CategoryProgressRow(
    category: String,
    amount: Double,
    totalSpent: Double,
    formatMoney: (Double) -> String
) {
    val progress = (amount / totalSpent).toFloat().coerceIn(0f, 1f)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = category)
            Text(text = formatMoney(amount))
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}