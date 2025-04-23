package com.example.pursepulse

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pursepulse.databinding.ActivityUpdateTransactionBinding

class UpdateTransaction : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTransactionBinding
    private lateinit var db: TransactionDatabaseHelper
    private var transactionId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TransactionDatabaseHelper(this)

        // Get transaction ID from intent
        transactionId = intent.getIntExtra("transaction_id", -1)
        if (transactionId == -1) {
            Toast.makeText(this, "Invalid transaction ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val categories = arrayOf("Food", "Transportation", "Entertainment", "Utilities", "Shopping", "Health", "Education", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter


        val transaction = db.getTransactionById(transactionId)
        if (transaction != null) {
            binding.editTextDate.setText(transaction.date)
            binding.editTextAmount.setText(transaction.amount.toString())

            // Set selected category in spinner
            val categoryPosition = categories.indexOf(transaction.category)
            if (categoryPosition != -1) {
                binding.spinnerCategory.setSelection(categoryPosition)
            }

            // Set the correct radio button based on the type
            if (transaction.type == "Income") {
                binding.radioGroupType.check(binding.radioIncome.id)
            } else {
                binding.radioGroupType.check(binding.radioExpense.id)
            }
        }

        // Set listener to update transaction
        binding.updatebtn.setOnClickListener {
            val date = binding.editTextDate.text.toString()
            val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
            val category = binding.spinnerCategory.selectedItem.toString()

            val selectedRadioId = binding.radioGroupType.checkedRadioButtonId
            val selectedType = if (selectedRadioId != -1) {
                findViewById<RadioButton>(selectedRadioId).text.toString()
            } else {
                ""
            }

            if (date.isEmpty() || amount == null || category.isEmpty() || selectedType.isEmpty()) {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTransaction = Transaction(
                id = transactionId,
                date = date,
                amount = amount,
                category = category,
                type = selectedType
            )

            val success = db.updateTransaction(updatedTransaction)
            if (success) {
                Toast.makeText(this, "Transaction updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update transaction", Toast.LENGTH_SHORT).show()
            }
        }
    }
}