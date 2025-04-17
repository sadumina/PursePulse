package com.example.pursepulse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Addtransction : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var dbHelper: TransactionDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addtransction)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize DB
        dbHelper = TransactionDatabaseHelper(this)

        // Load transactions
        loadTransactions()

        // Floating Action Button
        val fab: FloatingActionButton = findViewById(R.id.addtrans)
        fab.setOnClickListener {
            val intent = Intent(this, Edittransaction::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTransactions() // Reload list after coming back from Edittransaction
    }

    private fun loadTransactions() {
        val transactions = dbHelper.getAllTransactions().toMutableList()
        if (::transactionAdapter.isInitialized) {
            transactionAdapter.updateList(transactions)
        } else {
            transactionAdapter = TransactionAdapter(transactions) { transactionToDelete ->
                val success = dbHelper.deleteTransaction(transactionToDelete.id)
                if (success) loadTransactions()
            }
            recyclerView.adapter = transactionAdapter
        }
    }

}
