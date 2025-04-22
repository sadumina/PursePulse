package com.example.pursepulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var incomePieChart: PieChart
    private lateinit var dbHelper: TransactionDatabaseHelper

    private lateinit var totalIncomeText: TextView
    private lateinit var totalExpenseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = TransactionDatabaseHelper(this)

        pieChart = findViewById(R.id.pieChart)
        incomePieChart = findViewById(R.id.incomePieChart)

        totalIncomeText = findViewById(R.id.totalIncomeText)
        totalExpenseText = findViewById(R.id.totalExpenseText)

        setupPieChart()
        setupIncomePieChart()
        updateIncomeAndExpenseTotals()

        findViewById<Button>(R.id.editbtn).setOnClickListener {
            startActivity(Intent(this, Addtransction::class.java))
        }

        findViewById<Button>(R.id.setgoal).setOnClickListener {
            startActivity(Intent(this, set_budget::class.java))
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_transactions -> {
                    startActivity(Intent(this, Edittransaction::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, set_budget::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateIncomeAndExpenseTotals() {
        val transactions = dbHelper.getAllTransactions()
        val totalIncome = transactions.filter { it.type.equals("Income", ignoreCase = true) }
            .sumOf { it.amount }

        val totalExpense = transactions.filter { it.type.equals("Expense", ignoreCase = true) }
            .sumOf { it.amount }

        totalIncomeText.text = "Rs.%.2f".format(totalIncome)
        totalExpenseText.text = "Rs.%.2f".format(totalExpense)
    }

    private fun setupPieChart() {
        val transactions = dbHelper.getAllTransactions()
        val expenseTransactions = transactions.filter { it.type.equals("Expense", ignoreCase = true) }

        val categorySums = expenseTransactions.groupBy { it.category }
            .mapValues { it.value.sumOf { t -> t.amount } }

        val entries = ArrayList<PieEntry>()
        for ((category, total) in categorySums) {
            entries.add(PieEntry(total.toFloat(), category))
        }

        if (entries.isEmpty()) {
            pieChart.clear()
            pieChart.centerText = "No expense data"
            return
        }

        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 12f

        pieChart.data = PieData(dataSet)
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setCenterText("Expenses")
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun setupIncomePieChart() {
        val transactions = dbHelper.getAllTransactions()
        val incomeTransactions = transactions.filter { it.type.equals("Income", ignoreCase = true) }

        val categorySums = incomeTransactions.groupBy { it.category }
            .mapValues { it.value.sumOf { t -> t.amount } }

        val entries = ArrayList<PieEntry>()
        for ((category, total) in categorySums) {
            entries.add(PieEntry(total.toFloat(), category))
        }

        if (entries.isEmpty()) {
            incomePieChart.clear()
            incomePieChart.centerText = "No income data"
            return
        }

        val dataSet = PieDataSet(entries, "Income")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 12f

        incomePieChart.data = PieData(dataSet)
        incomePieChart.description.isEnabled = false
        incomePieChart.setUsePercentValues(true)
        incomePieChart.setDrawEntryLabels(true)
        incomePieChart.setEntryLabelTextSize(12f)
        incomePieChart.setCenterText("Income")
        incomePieChart.animateY(1000)
        incomePieChart.invalidate()
    }
}
