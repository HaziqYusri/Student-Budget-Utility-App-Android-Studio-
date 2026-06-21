package com.example.studentbudgetutility.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentbudgetutility.viewmodel.BudgetViewModel

@Composable
fun SettingsScreen(
    budgetViewModel: BudgetViewModel,
    onBackToHome: () -> Unit
) {
    var budgetText by remember {
        mutableStateOf(budgetViewModel.monthlyBudget.toString())
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = budgetText,
                onValueChange = { budgetText = it },
                label = { Text("Monthly Budget") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

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

            Text(
                text = "Currency",
                style = MaterialTheme.typography.titleMedium
            )

            CurrencySelector(
                selectedCurrency = budgetViewModel.selectedCurrency,
                onCurrencySelected = {
                    budgetViewModel.updateCurrency(it)
                }
            )

            Button(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun CurrencySelector(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    val currencies = listOf("USD", "AUD", "MYR", "CNY")

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        currencies.forEach { currency ->
            FilterChip(
                selected = selectedCurrency == currency,
                onClick = { onCurrencySelected(currency) },
                label = { Text(currency) }
            )
        }
    }
}