package com.example.studentbudgetutility.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.components.BudgetCard
import com.example.studentbudgetutility.components.CategorySummary
import com.example.studentbudgetutility.components.CompactQuickAddCategory
import com.example.studentbudgetutility.components.RecentTransactionsHeader
import com.example.studentbudgetutility.components.TransactionHistory
import com.example.studentbudgetutility.viewmodel.BudgetViewModel

@Composable
fun BudgetScreen(
    budgetViewModel: BudgetViewModel,
    onOpenSettings: () -> Unit
) {
    val expenses = budgetViewModel.expenses
    val monthlyBudget = budgetViewModel.monthlyBudget
    val spent = budgetViewModel.spent
    val remaining = budgetViewModel.remaining
    val safeDailySpend = budgetViewModel.safeDailySpend

    var showHistory by remember {
        mutableStateOf(true)
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Student Budget Utility",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Button(
                    onClick = onOpenSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Open Settings")
                }
            }

            item {
                BudgetCard("Monthly Budget", budgetViewModel.formatMoney(monthlyBudget))
            }

            item {
                BudgetCard("Spent This Month", budgetViewModel.formatMoney(spent))
            }

            item {
                BudgetCard("Remaining Balance", budgetViewModel.formatMoney(remaining))
            }

            item {
                BudgetCard("Safe Daily Spend", budgetViewModel.formatMoney(safeDailySpend))
            }

            item {
                Text(
                    text = "Selected Currency: ${budgetViewModel.selectedCurrency}",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            item {
                CategorySummary(
                    expenses = expenses,
                    formatMoney = { amount -> budgetViewModel.formatMoney(amount) }
                )
            }

            item {
                Text(
                    text = "Quick Add Expenses",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                CompactQuickAddCategory("Food") {
                    budgetViewModel.addExpense(it)
                }
            }

            item {
                CompactQuickAddCategory("Transport") {
                    budgetViewModel.addExpense(it)
                }
            }

            item {
                CompactQuickAddCategory("Entertainment") {
                    budgetViewModel.addExpense(it)
                }
            }

            item {
                CompactQuickAddCategory("Shopping") {
                    budgetViewModel.addExpense(it)
                }
            }

            item {
                RecentTransactionsHeader(
                    count = expenses.size,
                    showHistory = showHistory,
                    onToggle = { showHistory = !showHistory }
                )
            }

            if (showHistory) {
                item {
                    TransactionHistory(
                        expenses = expenses,
                        formatMoney = { amount -> budgetViewModel.formatMoney(amount) }
                    )
                }
            }
        }
    }
}