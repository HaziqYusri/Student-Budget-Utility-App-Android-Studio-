package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.data.sampleExpenses
import com.example.studentbudgetutility.model.Expense
import com.example.studentbudgetutility.ui.theme.StudentBudgetUtilityTheme
import com.example.studentbudgetutility.util.formatDate
import com.example.studentbudgetutility.util.formatTime

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

@Composable
fun BudgetCard(title: String, amount: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = amount,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun CompactQuickAddCategory(
    category: String,
    onAddExpense: (Expense) -> Unit
) {
    var customAmountText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickAddButton("+5") {
                    onAddExpense(Expense(category, 5.0, System.currentTimeMillis()))
                }

                QuickAddButton("+10") {
                    onAddExpense(Expense(category, 10.0, System.currentTimeMillis()))
                }

                QuickAddButton("+15") {
                    onAddExpense(Expense(category, 15.0, System.currentTimeMillis()))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customAmountText,
                    onValueChange = { customAmountText = it },
                    label = { Text("Custom") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )

                Button(
                    onClick = {
                        val amount = customAmountText.toDoubleOrNull()

                        if (amount != null && amount > 0) {
                            onAddExpense(
                                Expense(
                                    category = category,
                                    amount = amount,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
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
fun RowScope.QuickAddButton(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(vertical = 6.dp)
    ) {
        Text(label)
    }
}

@Composable
fun RecentTransactionsHeader(
    count: Int,
    showHistory: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Transactions ($count)",
            style = MaterialTheme.typography.titleLarge
        )

        TextButton(onClick = onToggle) {
            Text(if (showHistory) "Hide" else "Show")
        }
    }
}

@Composable
fun TransactionHistory(expenses: List<Expense>) {
    val groupedExpenses = expenses
        .sortedByDescending { it.timestamp }
        .groupBy { formatDate(it.timestamp) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        groupedExpenses.forEach { (date, expensesForDate) ->
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium
            )

            expensesForDate.forEach { expense ->
                ExpenseRow(expense)
            }
        }
    }
}

@Composable
fun ExpenseRow(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = expense.category)
                Text(
                    text = formatTime(expense.timestamp),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(text = "$${"%.2f".format(expense.amount)}")
        }
    }
}