package com.example.studentbudgetutility.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun CategorySummary(expenses: List<Expense>) {
    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Category Summary",
                style = MaterialTheme.typography.titleLarge
            )

            CategorySummaryRow("Food", categoryTotals["Food"] ?: 0.0)
            CategorySummaryRow("Transport", categoryTotals["Transport"] ?: 0.0)
            CategorySummaryRow("Entertainment", categoryTotals["Entertainment"] ?: 0.0)
            CategorySummaryRow("Shopping", categoryTotals["Shopping"] ?: 0.0)
        }
    }
}

@Composable
fun CategorySummaryRow(category: String, amount: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = category)
        Text(text = "$${"%.2f".format(amount)}")
    }
}