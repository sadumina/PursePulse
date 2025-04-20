package com.example.pursepulse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class set_budget : AppCompatActivity() {

    private var monthlyBudget = 0
    private lateinit var dbHelper: TransactionDatabaseHelper
    private lateinit var spentText: TextView
    private lateinit var budgetProgress: ProgressBar
    private lateinit var warningText: TextView
    private lateinit var budgetInput: EditText
    private lateinit var setBudgetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_set_budget)

        dbHelper = TransactionDatabaseHelper(this)

        budgetInput = findViewById(R.id.budgetInput)
        setBudgetButton = findViewById(R.id.setBudgetButton)
        spentText = findViewById(R.id.spentText)
        budgetProgress = findViewById(R.id.budgetProgress)
        warningText = findViewById(R.id.warningText)

        // Set budget button click
        setBudgetButton.setOnClickListener {
            val input = budgetInput.text.toString()
            if (input.isNotEmpty()) {
                monthlyBudget = input.toInt()

                // Save the monthly budget in SharedPreferences
                val sharedPref = getSharedPreferences("BudgetPrefs", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("monthly_budget", monthlyBudget)
                editor.putFloat("total_income", 2500f)  // Example income
                editor.putFloat("total_expense", 1500f) // Example expense
                editor.apply()

                updateProgressBar()
            } else {
                Toast.makeText(this, "Please enter a valid budget", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProgressBar() {
        if (monthlyBudget == 0) return

        val allTransactions = dbHelper.getAllTransactions()
        val totalSpending = allTransactions
            .filter { it.type.lowercase() == "expense" }
            .sumOf { it.amount }

        val percentage = ((totalSpending / monthlyBudget) * 100).toInt()
        spentText.text = "Spent: Rs${"%.2f".format(totalSpending)} / Rs$monthlyBudget"
        budgetProgress.progress = percentage.coerceAtMost(100)

        when {
            percentage >= 100 -> {
                warningText.visibility = View.VISIBLE
                warningText.text = "⚠ Budget Exceeded!"
            }
            percentage >= 80 -> {
                warningText.visibility = View.VISIBLE
                warningText.text = "⚠ You’re close to your budget!"
            }
            else -> {
                warningText.visibility = View.GONE
            }
        }
    }
}
