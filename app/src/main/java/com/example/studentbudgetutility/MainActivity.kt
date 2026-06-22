package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentbudgetutility.screens.BudgetScreen
import com.example.studentbudgetutility.screens.MonthlyStatsScreen
import com.example.studentbudgetutility.screens.SettingsScreen
import com.example.studentbudgetutility.ui.theme.StudentBudgetUtilityTheme
import com.example.studentbudgetutility.viewmodel.BudgetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StudentBudgetUtilityTheme {
                val budgetViewModel: BudgetViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("home") }

                when (currentScreen) {
                    "home" -> BudgetScreen(
                        budgetViewModel = budgetViewModel,
                        onOpenSettings = { currentScreen = "settings" },
                        onOpenStats = { currentScreen = "stats" }
                    )

                    "settings" -> SettingsScreen(
                        budgetViewModel = budgetViewModel,
                        onBackToHome = { currentScreen = "home" }
                    )

                    "stats" -> MonthlyStatsScreen(
                        budgetViewModel = budgetViewModel,
                        onBackToHome = { currentScreen = "home" }
                    )
                }
            }
        }
    }
}