package com.example.pursepulse

data class Transaction(
             val id: Int = 0,
             val date: String,
             val amount: Double,
             val category: String,
             val type: String
)
