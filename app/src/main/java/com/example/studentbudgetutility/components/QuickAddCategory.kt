package com.example.studentbudgetutility.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.model.Expense

@Composable
fun CompactQuickAddCategory(
    category: String,
    onAddExpense: (Expense) -> Unit
) {
    var customAmountText by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

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
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
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

                TextButton(
                    onClick = {
                        showCustomInput = !showCustomInput
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Text(if (showCustomInput) "Hide" else "Custom")
                }
            }

            if (showCustomInput) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customAmountText,
                        onValueChange = { customAmountText = it },
                        label = { Text("Custom SGD") },
                        prefix = { Text("S$") },
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
                                showCustomInput = false
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