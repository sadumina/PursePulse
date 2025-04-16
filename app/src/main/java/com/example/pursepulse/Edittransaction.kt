package com.example.pursepulse

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Edittransaction : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private val sharedPrefsFile = "TransactionPrefs"
    private val categoryKey = "selectedCategory"
    private var isFirstSelection = true // To prevent toast on first load

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edittransaction)

        spinner = findViewById(R.id.spinnerCategory)

        val categories = listOf("Food", "Transport", "Bills", "Entertainment")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        spinner.adapter = adapter

        val prefs = getSharedPreferences(sharedPrefsFile, Context.MODE_PRIVATE)
        val savedCategory = prefs.getString(categoryKey, null)
        if (savedCategory != null) {
            val position = categories.indexOf(savedCategory)
            if (position >= 0) {
                spinner.setSelection(position)
            }
        }

        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                if (!isFirstSelection) {
                    val selectedCategory = categories[position]
                    prefs.edit().putString(categoryKey, selectedCategory).apply()
                    Toast.makeText(this@Edittransaction, "Category saved: $selectedCategory", Toast.LENGTH_SHORT).show()
                }
                isFirstSelection = false
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // No-op
            }
        })
    }
}
