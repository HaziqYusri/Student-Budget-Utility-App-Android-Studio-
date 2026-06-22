package com.example.studentbudgetutility.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.viewmodel.BudgetViewModel
import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun SettingsScreen(
    budgetViewModel: BudgetViewModel,
    onBackToHome: () -> Unit
) {
    var budgetText by remember {
        mutableStateOf(budgetViewModel.monthlyBudget.toString())
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Text(
                    text = "Monthly budget is entered in SGD",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            item {
                OutlinedTextField(
                    value = budgetText,
                    onValueChange = { budgetText = it },
                    label = { Text("Monthly Budget (SGD)") },
                    prefix = { Text("S$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {
                        val newBudget = budgetText.toDoubleOrNull()
                        if (newBudget != null && newBudget > 0) {
                            budgetViewModel.updateMonthlyBudget(newBudget)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Budget")
                }
            }

            item {
                Text(
                    text = "Display Currency and Conversion Rates",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Text(
                    text = "Rates are based on 1 SGD. Select a display currency and edit its rate if needed.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            item {
                CurrencyRateSelector(
                    selectedCurrency = budgetViewModel.selectedCurrency,
                    conversionRates = budgetViewModel.conversionRates,
                    onCurrencySelected = { budgetViewModel.updateCurrency(it) },
                    onRateChanged = { currency, rate ->
                        budgetViewModel.updateConversionRate(currency, rate)
                    }
                )
            }

            item {
                OutlinedButton(
                    onClick = {
                        budgetViewModel.resetConversionRates()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Conversion Rates")
                }
            }

            item {
                Text(
                    text = "Transaction Controls",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                var showClearConfirmation by remember {
                    mutableStateOf(false)
                }

                if (!showClearConfirmation) {
                    OutlinedButton(
                        onClick = {
                            showClearConfirmation = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear All Transactions")
                    }
                } else {
                    Text(
                        text = "Are you sure you want to clear all transactions?",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = {
                            budgetViewModel.clearAllExpenses()
                            showClearConfirmation = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Yes, Clear All")
                    }

                    OutlinedButton(
                        onClick = {
                            showClearConfirmation = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
            item {
                OutlinedButton(
                    onClick = { budgetViewModel.resetSampleExpenses() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Sample Transactions")
                }
            }

            item {
                if (budgetViewModel.settingsMessage.isNotBlank()) {
                    Text(
                        text = budgetViewModel.settingsMessage,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
fun CurrencyRateSelector(
    selectedCurrency: String,
    conversionRates: Map<String, Double>,
    onCurrencySelected: (String) -> Unit,
    onRateChanged: (String, Double) -> Unit
) {
    val currencies = listOf("SGD", "USD", "AUD", "MYR", "CNY", "UZS")



    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        currencies.forEach { currency ->
            var rateText by remember(currency) {
                mutableStateOf(conversionRates[currency]?.toString() ?: "1.0")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedCurrency == currency,
                    onClick = { onCurrencySelected(currency) },
                    label = { Text(currency) },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = rateText,
                    onValueChange = { newText ->
                        rateText = newText
                        val newRate = newText.toDoubleOrNull()
                        if (newRate != null && newRate > 0) {
                            onRateChanged(currency, newRate)
                        }
                    },
                    label = { Text("Rate") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}