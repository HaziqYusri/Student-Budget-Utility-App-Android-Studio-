package com.example.studentbudgetutility.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.model.Expense
import com.example.studentbudgetutility.util.formatDate

@Composable
fun TransactionHistory(
    expenses: List<Expense>,
    formatMoney: (Double) -> String,
    onDeleteExpense: (Expense) -> Unit
) {
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
                ExpenseRow(
                    expense = expense,
                    formatMoney = formatMoney,
                    onDeleteExpense = onDeleteExpense
                )
            }
        }
    }
}