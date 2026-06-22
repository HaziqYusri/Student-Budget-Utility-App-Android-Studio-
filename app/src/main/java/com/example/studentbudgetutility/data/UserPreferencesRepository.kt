package com.example.studentbudgetutility.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.studentbudgetutility.model.Expense
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val monthlyBudget: Double = 1000.0,
    val selectedCurrency: String = "SGD",
    val cycleLengthDays: Int = 30,
    val cycleStartTimeMillis: Long = System.currentTimeMillis(),
    val cycleEndTimeMillis: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
    val expenses: List<Expense> = sampleExpenses(),
    val conversionRates: Map<String, Double> = mapOf(
        "SGD" to 1.0,
        "USD" to 0.78,
        "AUD" to 1.20,
        "MYR" to 3.30,
        "CNY" to 5.60,
        "UZS" to 10300.0
    )
)

class UserPreferencesRepository(
    private val context: Context
) {
    private object Keys {
        val MONTHLY_BUDGET = doublePreferencesKey("monthly_budget")
        val SELECTED_CURRENCY = stringPreferencesKey("selected_currency")
        val CYCLE_LENGTH_DAYS = intPreferencesKey("cycle_length_days")
        val CYCLE_START = longPreferencesKey("cycle_start")
        val CYCLE_END = longPreferencesKey("cycle_end")
        val EXPENSES_JSON = stringPreferencesKey("expenses_json")

        val SGD_RATE = doublePreferencesKey("sgd_rate")
        val USD_RATE = doublePreferencesKey("usd_rate")
        val AUD_RATE = doublePreferencesKey("aud_rate")
        val MYR_RATE = doublePreferencesKey("myr_rate")
        val CNY_RATE = doublePreferencesKey("cny_rate")
        val UZS_RATE = doublePreferencesKey("uzs_rate")
    }

    val userPreferencesFlow = context.dataStore.data.map { preferences ->
        UserPreferences(
            monthlyBudget = preferences[Keys.MONTHLY_BUDGET] ?: 1000.0,
            selectedCurrency = preferences[Keys.SELECTED_CURRENCY] ?: "SGD",
            cycleLengthDays = preferences[Keys.CYCLE_LENGTH_DAYS] ?: 30,
            cycleStartTimeMillis = preferences[Keys.CYCLE_START] ?: System.currentTimeMillis(),
            cycleEndTimeMillis = preferences[Keys.CYCLE_END]
                ?: System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
            expenses = decodeExpenses(preferences[Keys.EXPENSES_JSON]),
            conversionRates = mapOf(
                "SGD" to (preferences[Keys.SGD_RATE] ?: 1.0),
                "USD" to (preferences[Keys.USD_RATE] ?: 0.78),
                "AUD" to (preferences[Keys.AUD_RATE] ?: 1.20),
                "MYR" to (preferences[Keys.MYR_RATE] ?: 3.30),
                "CNY" to (preferences[Keys.CNY_RATE] ?: 5.60),
                "UZS" to (preferences[Keys.UZS_RATE] ?: 10300.0)
            )
        )
    }

    suspend fun saveExpenses(expenses: List<Expense>) {
        context.dataStore.edit {
            it[Keys.EXPENSES_JSON] = encodeExpenses(expenses)
        }
    }

    suspend fun saveMonthlyBudget(value: Double) {
        context.dataStore.edit { it[Keys.MONTHLY_BUDGET] = value }
    }

    suspend fun saveSelectedCurrency(value: String) {
        context.dataStore.edit { it[Keys.SELECTED_CURRENCY] = value }
    }

    suspend fun saveCycleLengthDays(value: Int) {
        context.dataStore.edit { it[Keys.CYCLE_LENGTH_DAYS] = value }
    }

    suspend fun saveCycleDates(start: Long, end: Long) {
        context.dataStore.edit {
            it[Keys.CYCLE_START] = start
            it[Keys.CYCLE_END] = end
        }
    }

    suspend fun saveConversionRate(currency: String, rate: Double) {
        context.dataStore.edit {
            when (currency) {
                "SGD" -> it[Keys.SGD_RATE] = rate
                "USD" -> it[Keys.USD_RATE] = rate
                "AUD" -> it[Keys.AUD_RATE] = rate
                "MYR" -> it[Keys.MYR_RATE] = rate
                "CNY" -> it[Keys.CNY_RATE] = rate
                "UZS" -> it[Keys.UZS_RATE] = rate
            }
        }
    }

    suspend fun saveAllConversionRates(rates: Map<String, Double>) {
        context.dataStore.edit {
            it[Keys.SGD_RATE] = rates["SGD"] ?: 1.0
            it[Keys.USD_RATE] = rates["USD"] ?: 0.78
            it[Keys.AUD_RATE] = rates["AUD"] ?: 1.20
            it[Keys.MYR_RATE] = rates["MYR"] ?: 3.30
            it[Keys.CNY_RATE] = rates["CNY"] ?: 5.60
            it[Keys.UZS_RATE] = rates["UZS"] ?: 10300.0
        }
    }

    private fun encodeExpenses(expenses: List<Expense>): String {
        val array = JSONArray()

        expenses.forEach { expense ->
            val json = JSONObject()
            json.put("id", expense.id)
            json.put("category", expense.category)
            json.put("amount", expense.amount)
            json.put("timestamp", expense.timestamp)
            array.put(json)
        }

        return array.toString()
    }

    private fun decodeExpenses(jsonString: String?): List<Expense> {
        if (jsonString.isNullOrBlank()) return sampleExpenses()

        return try {
            val array = JSONArray(jsonString)
            val expenses = mutableListOf<Expense>()

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                expenses.add(
                    Expense(
                        id = item.getLong("id"),
                        category = item.getString("category"),
                        amount = item.getDouble("amount"),
                        timestamp = item.getLong("timestamp")
                    )
                )
            }

            expenses
        } catch (e: Exception) {
            sampleExpenses()
        }
    }
}