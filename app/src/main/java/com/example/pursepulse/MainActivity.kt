package com.example.pursepulse


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnNavigate: Button = findViewById(R.id.buttonnavigate)
        btnNavigate.setOnClickListener {
            Toast.makeText(this, "Getstarted!", Toast.LENGTH_SHORT).show() // Debugging

            val intent = Intent(this, Addtransction::class.java)
            startActivity(intent)
        }

    }
}