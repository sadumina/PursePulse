package com.example.pursepulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Handle "+ Add" button
        val btnNavigate: Button = findViewById(R.id.editbtn)
        btnNavigate.setOnClickListener {
            Toast.makeText(this, "Get started!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Addtransction::class.java)
            startActivity(intent)
        }

        // Handle "Set Monthly Budget" button
        val btnSetGoal: Button = findViewById(R.id.setgoal)
        btnSetGoal.setOnClickListener {
            Toast.makeText(this, "Setting your goal!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, set_budget::class.java)
            startActivity(intent)
        }

        // Handle Bottom Navigation
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_transactions -> {
                    Toast.makeText(this, "Transactions", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Edittransaction::class.java))

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
