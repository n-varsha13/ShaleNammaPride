package com.example.shale_nammapride.model

data class BudgetTransaction(
    val id: String = "",
    val type: String = "", // "Income" or "Expenditure"
    val amount: Double = 0.0,
    val description: String = "",
    val date: String = "",
    val timestamp: Long = 0L
)