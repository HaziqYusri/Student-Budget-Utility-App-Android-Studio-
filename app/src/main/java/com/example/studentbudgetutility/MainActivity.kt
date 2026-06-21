package com.example.studentbudgetutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studentbudgetutility.screens.BudgetScreen
import com.example.studentbudgetutility.ui.theme.StudentBudgetUtilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StudentBudgetUtilityTheme {
                BudgetScreen()
            }
        }
    }
}