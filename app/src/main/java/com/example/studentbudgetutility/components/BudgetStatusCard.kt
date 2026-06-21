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

@Composable
fun BudgetStatusCard(
    spent: Double,
    monthlyBudget: Double,
    remaining: Double,
    formatMoney: (Double) -> String
) {
    val percentageUsed = if (monthlyBudget > 0) {
        spent / monthlyBudget
    } else {
        0.0
    }

    val statusMessage = when {
        remaining < 0 -> "You are over budget. Reduce spending where possible."
        percentageUsed >= 0.8 -> "You are close to your monthly budget limit."
        percentageUsed >= 0.5 -> "You have used more than half of your budget."
        else -> "You are on track with your monthly budget."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Budget Status",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Remaining: ${formatMoney(remaining)}",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}