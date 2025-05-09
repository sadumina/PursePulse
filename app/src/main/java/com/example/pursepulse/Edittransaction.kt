package com.example.pursepulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pursepulse.databinding.ActivityEdittransactionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Edittransaction : AppCompatActivity() {

    private lateinit var binding: ActivityEdittransactionBinding
    private lateinit var db : TransactionDatabaseHelper
    private val sharedPrefsFile = "TransactionPrefs"
    private val categoryKey = "selectedCategory"
    private var isFirstSelection = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityEdittransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val categories = listOf("Food", "Transport", "Bills", "Entertainment" , "Salary", "Business", "Freelancing", "Investments", "Gifts", "Rental Income", "Interest", "Dividends", "Cashback", "Selling Stuff", "Refunds", "Other Income")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        binding.spinnerCategory.adapter = adapter


        val prefs = getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)
        val savedCategory = prefs.getString(categoryKey, null)
        savedCategory?.let {
            val position = categories.indexOf(it)
            if (position >= 0) binding.spinnerCategory.setSelection(position)
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                if (!isFirstSelection) {
                    val selectedCategory = categories[position]
                    prefs.edit().putString(categoryKey, selectedCategory).apply()
                    Toast.makeText(
                        this@Edittransaction,
                        "Category saved: $selectedCategory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isFirstSelection = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.saveButton.setOnClickListener {
            val date = binding.editTextDate.text.toString()
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
            val category = binding.spinnerCategory.selectedItem.toString()
            val type = when {
                binding.radioExpense.isChecked -> "Expense"
                binding.radioIncome.isChecked -> "Income"
                else -> ""
            }

            if (date.isNotEmpty() && amount != null && type.isNotEmpty()) {
                val transaction = Transaction(0, date, amount, category, type)
                val dbHelper = TransactionDatabaseHelper(this)
                val success = dbHelper.insertTransaction(transaction)

                if (success) {
                    Toast.makeText(this, "Transaction saved", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Addtransction::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save transaction", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Home::class.java))
                    true
                }
                R.id.nav_transactions -> {
                    Toast.makeText(this, "Transactions", Toast.LENGTH_SHORT).show()


                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, set_budget::class.java))


                    true
                }
                else -> false
            }
        }

    }

}
