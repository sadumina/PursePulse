package com.example.pursepulse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)




        val btnNavigate: Button = findViewById(R.id.editbtn)
        btnNavigate.setOnClickListener {
            Toast.makeText(this, "Get started!", Toast.LENGTH_SHORT).show() // Debugging
            val intent = Intent(this, Addtransction::class.java)
            startActivity(intent)
        }


        val btnsetgoal: Button = findViewById(R.id.setgoal)
        btnsetgoal.setOnClickListener {
            Toast.makeText(this, "Get started!", Toast.LENGTH_SHORT).show() // Debugging
            val intent = Intent(this, set_budget::class.java)
            startActivity(intent)
        }
    }
}
