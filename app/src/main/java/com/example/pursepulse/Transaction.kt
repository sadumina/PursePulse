package com.example.pursepulse

data class Transaction(




val id: Int = 0, // Primary key, auto-incremented

val date: String,
val amount: Double,
val category: String,
val type: String // "Expense" or "Income"

)
