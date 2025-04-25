package com.example.pursepulse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView

private object BudgetPrefs {
    private const val FILE = "BudgetPrefs"
    private const val KEY = "monthly_budget"
    fun read(ctx: android.content.Context): Int =
        ctx.getSharedPreferences(FILE, android.content.Context.MODE_PRIVATE)
            .getInt(KEY, 0)
}

class Home : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var incomePieChart: PieChart
    private lateinit var dbHelper: TransactionDatabaseHelper

    private lateinit var totalIncomeText: TextView
    private lateinit var totalExpenseText: TextView

    private lateinit var budgetProgress: ProgressBar
    private lateinit var budgetWarning: TextView

    private var hasNotified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = TransactionDatabaseHelper(this)

        pieChart = findViewById(R.id.pieChart)
        incomePieChart = findViewById(R.id.incomePieChart)
        totalIncomeText = findViewById(R.id.totalIncomeText)
        totalExpenseText = findViewById(R.id.totalExpenseText)
        budgetProgress = findViewById(R.id.budgetProgress)
        budgetWarning = findViewById(R.id.budgetWarning)

        findViewById<Button>(R.id.editbtn).setOnClickListener {
            startActivity(Intent(this, Addtransction::class.java))
        }
        findViewById<Button>(R.id.setgoal).setOnClickListener {
            startActivity(Intent(this, set_budget::class.java))
        }

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
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

        // Notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateIncomeAndExpenseTotals()
        setupPieChart()
        setupIncomePieChart()
        updateBudgetProgress()
    }

    private fun updateBudgetProgress() {
        val budget = BudgetPrefs.read(this)
        if (budget == 0) {
            budgetProgress.visibility = View.GONE
            budgetWarning.text = ""
            return
        }

        val notificationHelper = NotificationHelper(this)

        budgetProgress.visibility = View.VISIBLE

        val spent = dbHelper.getAllTransactions()
            .filter { it.type.equals("Expense", true) }
            .sumOf { it.amount }

        val percent = ((spent / budget) * 100).toInt().coerceAtMost(100)
        budgetProgress.progress = percent

        budgetWarning.text = when {
            percent >= 100 -> {
                if (!hasNotified) {
                    notificationHelper.showNotification(
                        "Budget Exceeded!",
                        "You’ve gone over your monthly budget!"
                    )
                    hasNotified = true
                }
                " Budget Exceeded!"
            }
            percent >= 80 -> {
                if (!hasNotified) {
                    notificationHelper.showNotification(
                        "Warning: Near Budget Limit",
                        "You're over 80% of your monthly budget."
                    )
                    hasNotified = true
                }
                " You’re close to your budget!"
            }
            else -> {
                hasNotified = false
                ""
            }
        }
    }

    private fun updateIncomeAndExpenseTotals() {
        val tx = dbHelper.getAllTransactions()
        val income = tx.filter { it.type.equals("Income", true) }.sumOf { it.amount }
        val expense = tx.filter { it.type.equals("Expense", true) }.sumOf { it.amount }

        totalIncomeText.text = "Rs.%.2f".format(income)
        totalExpenseText.text = "Rs.%.2f".format(expense)
    }

    private fun setupPieChart() = loadPie(
        chart = pieChart,
        entries = buildEntries("Expense"),
        title = "Expenses",
        colors = ColorTemplate.MATERIAL_COLORS.toList()
    )

    private fun setupIncomePieChart() = loadPie(
        chart = incomePieChart,
        entries = buildEntries("Income"),
        title = "Income",
        colors = ColorTemplate.COLORFUL_COLORS.toList()
    )

    private fun buildEntries(type: String): List<PieEntry> =
        dbHelper.getAllTransactions()
            .filter { it.type.equals(type, true) }
            .groupBy { it.category }
            .map { (cat, list) -> PieEntry(list.sumOf { it.amount }.toFloat(), cat) }

    private fun loadPie(
        chart: PieChart,
        entries: List<PieEntry>,
        title: String,
        colors: List<Int>
    ) {
        if (entries.isEmpty()) {
            chart.clear(); chart.centerText = "No $title data"; return
        }
        val ds = PieDataSet(entries, title).apply {
            this.colors = colors
            sliceSpace = 2f
            valueTextSize = 12f
        }
        chart.data = PieData(ds)
        chart.description.isEnabled = false
        chart.setUsePercentValues(true)
        chart.setDrawEntryLabels(true)
        chart.setEntryLabelTextSize(12f)
        chart.setCenterText(title)
        chart.animateY(1000)
        chart.invalidate()
    }
}
