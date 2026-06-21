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
fun DailySpendingCard(
    safeDailySpend: Double,
    formatMoney: (Double) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Daily Spending Guide",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Try to stay below ${formatMoney(safeDailySpend)} per day for the rest of the month.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}