package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.components.BudgetCard
import com.example.studentbudgetutility.components.CompactQuickAddCategory
import com.example.studentbudgetutility.components.RecentTransactionsHeader
import com.example.studentbudgetutility.components.TransactionHistory
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.ui.theme.StudentBudgetUtilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentBudgetUtilityTheme {
                BudgetMainScreen()
            }
        }
    }
}

@Composable
fun BudgetMainScreen() {
    val monthlyBudget = 1000.0

    var expenses by remember {
        mutableStateOf(sampleExpenses())
    }

    var showHistory by remember {
        mutableStateOf(true)
    }

    val spent = expenses.sumOf { it.amount }
    val remaining = monthlyBudget - spent
    val safeDailySpend = remaining / 30

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
                BudgetCard("Monthly Budget", "$${"%.2f".format(monthlyBudget)}")
            }

            item {
                BudgetCard("Spent This Month", "$${"%.2f".format(spent)}")
            }

            item {
                BudgetCard("Remaining Balance", "$${"%.2f".format(remaining)}")
            }

            item {
                BudgetCard("Safe Daily Spend", "$${"%.2f".format(safeDailySpend)}")
            }

            item {
                Text(
                    text = "Quick Add Expenses",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                CompactQuickAddCategory("Food") {
                    expenses = expenses + it
                }
            }

            item {
                CompactQuickAddCategory("Transport") {
                    expenses = expenses + it
                }
            }

            item {
                CompactQuickAddCategory("Entertainment") {
                    expenses = expenses + it
                }
            }

            item {
                CompactQuickAddCategory("Shopping") {
                    expenses = expenses + it
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
                    TransactionHistory(expenses)
                }
            }
        }
    }
}