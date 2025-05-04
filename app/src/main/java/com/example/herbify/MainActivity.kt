package com.example.herbify

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = findViewById<EditText>(R.id.fullName)
        val password = findViewById<EditText>(R.id.passwordEditText)
        val loginbtn = findViewById<MaterialButton>(R.id.login)
        val signbtn = findViewById<TextView>(R.id.signin)

        loginbtn.setOnClickListener {
            val usernameText = name.text.toString().trim()
            val passwordText = password.text.toString().trim()

            when {
                usernameText.isEmpty() -> {
                    Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                }
                passwordText.isEmpty() -> {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }
            }
        }

        signbtn.setOnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        }
    }
}
