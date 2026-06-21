package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val expenses = listOf(
        Expense("Food", 15.0),
        Expense("Transport", 8.0),
        Expense("Coffee", 6.0)
    )

    val spent = expenses.sumOf { it.amount }
    val remaining = monthlyBudget - spent
    val safeDailySpend = remaining / 30

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Student Budget Utility",
                style = MaterialTheme.typography.headlineMedium
            )

            BudgetCard("Monthly Budget", "$${"%.2f".format(monthlyBudget)}")
            BudgetCard("Spent This Month", "$${"%.2f".format(spent)}")
            BudgetCard("Remaining Balance", "$${"%.2f".format(remaining)}")
            BudgetCard("Safe Daily Spend", "$${"%.2f".format(safeDailySpend)}")

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.headlineSmall
            )

            ExpenseList(expenses)
        }
    }
}

@Composable
fun BudgetCard(title: String, amount: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun ExpenseList(expenses: List<Expense>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expenses.forEach { expense ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = expense.category)
                    Text(text = "$${"%.2f".format(expense.amount)}")
                }
            }
        }
    }
}

data class Expense(
    val category: String,
    val amount: Double
)