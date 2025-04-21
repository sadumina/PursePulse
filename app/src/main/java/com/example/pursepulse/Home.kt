package com.example.pursepulse

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set up custom toolbar
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val btnNavigate: Button = findViewById(R.id.editbtn)
        btnNavigate.setOnClickListener {
            Toast.makeText(this, "Get started!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Addtransction::class.java)
            startActivity(intent)
        }

        val btnsetgoal: Button = findViewById(R.id.setgoal)
        btnsetgoal.setOnClickListener {
            Toast.makeText(this, "Setting your goal!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, set_budget::class.java)
            startActivity(intent)
        }
    }

    // Show the menu (top-right)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    // Handle menu clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.nav_transactions -> {
                Toast.makeText(this, "Transactions clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.nav_budget -> {
                Toast.makeText(this, "Budget clicked", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.nav_reports -> {
                Toast.makeText(this, "Reports clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
