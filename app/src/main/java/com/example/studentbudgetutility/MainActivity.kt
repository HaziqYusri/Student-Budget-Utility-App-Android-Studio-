package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

    var expenses by remember {
        mutableStateOf(
            listOf(
                Expense("Food", 15.0),
                Expense("Transport", 8.0),
                Expense("Coffee", 6.0)
            )
        )
    }

    val spent = expenses.sumOf { it.amount }
    val remaining = monthlyBudget - spent
    val safeDailySpend = remaining / 30

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Student Budget Utility",
                    style = MaterialTheme.typography.headlineMedium
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
                QuickAddCategory(
                    category = "Food",
                    onAddExpense = { expense ->
                        expenses = expenses + expense
                    }
                )
            }

            item {
                QuickAddCategory(
                    category = "Transport",
                    onAddExpense = { expense ->
                        expenses = expenses + expense
                    }
                )
            }

            item {
                QuickAddCategory(
                    category = "Entertainment",
                    onAddExpense = { expense ->
                        expenses = expenses + expense
                    }
                )
            }

            item {
                QuickAddCategory(
                    category = "Shopping",
                    onAddExpense = { expense ->
                        expenses = expenses + expense
                    }
                )
            }

            item {
                Text(
                    text = "Recent Expenses",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            items(expenses) { expense ->
                ExpenseRow(expense)
            }
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
fun QuickAddCategory(
    category: String,
    onAddExpense: (Expense) -> Unit
) {
    var customAmountText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onAddExpense(Expense(category, 5.0)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+$5")
                }

                Button(
                    onClick = { onAddExpense(Expense(category, 10.0)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+$10")
                }

                Button(
                    onClick = { onAddExpense(Expense(category, 15.0)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+$15")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { customAmountText = it },
                    label = { Text("Custom amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(2f),
                    singleLine = true
                )

                Button(
                    onClick = {
                        val customAmount = customAmountText.toDoubleOrNull()

                        if (customAmount != null && customAmount > 0) {
                            onAddExpense(Expense(category, customAmount))
                            customAmountText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun ExpenseRow(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = expense.category)
            Text(text = "$${"%.2f".format(expense.amount)}")
        }
    }
}

data class Expense(
    val category: String,
    val amount: Double
)