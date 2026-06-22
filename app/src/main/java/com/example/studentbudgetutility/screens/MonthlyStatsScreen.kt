package com.example.studentbudgetutility.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.viewmodel.BudgetViewModel

@Composable
fun MonthlyStatsScreen(
    budgetViewModel: BudgetViewModel,
    onBackToHome: () -> Unit
) {
    BackHandler {
        onBackToHome()
    }

    val expenses = budgetViewModel.expenses
    val totalSpent = budgetViewModel.spent
    val averageExpense = if (expenses.isNotEmpty()) totalSpent / expenses.size else 0.0

    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    val highestCategory = categoryTotals
        .maxByOrNull { it.value }
        ?.key ?: "No spending yet"

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Monthly Stats",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    TextButton(onClick = onBackToHome) {
                        Text("← Home")
                    }
                }
            }

            item {
                MonthlyStatCard(
                    title = "Total Spent",
                    value = budgetViewModel.formatMoney(totalSpent)
                )
            }

            item {
                MonthlyStatCard(
                    title = "Remaining Budget",
                    value = budgetViewModel.formatMoney(budgetViewModel.remaining)
                )
            }

            item {
                MonthlyStatCard(
                    title = "Transactions",
                    value = expenses.size.toString()
                )
            }

            item {
                MonthlyStatCard(
                    title = "Average Expense",
                    value = budgetViewModel.formatMoney(averageExpense)
                )
            }

            item {
                MonthlyStatCard(
                    title = "Highest Spending Category",
                    value = highestCategory
                )
            }

            item {
                Text(
                    text = "Category Breakdown",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                CategoryStatsList(
                    categoryTotals = categoryTotals,
                    totalSpent = totalSpent,
                    formatMoney = { amount -> budgetViewModel.formatMoney(amount) }
                )
            }

            item {
                Button(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}

@Composable
fun MonthlyStatCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun CategoryStatsList(
    categoryTotals: Map<String, Double>,
    totalSpent: Double,
    formatMoney: (Double) -> String
) {
    val categories = listOf("Food", "Transport", "Entertainment", "Shopping")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categories.forEach { category ->
                val amount = categoryTotals[category] ?: 0.0
                val progress = if (totalSpent > 0) {
                    (amount / totalSpent).toFloat().coerceIn(0f, 1f)
                } else {
                    0f
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(category)
                        Text(formatMoney(amount))
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}