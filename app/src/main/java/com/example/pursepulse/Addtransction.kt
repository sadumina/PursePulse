package com.example.pursepulse


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Addtransction : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_addtransction)


        val fab: FloatingActionButton = findViewById(R.id.addtrans)

        fab.setOnClickListener {
            val intent = Intent(this, Edittransaction::class.java)
            startActivity(intent)
        }
    }
}