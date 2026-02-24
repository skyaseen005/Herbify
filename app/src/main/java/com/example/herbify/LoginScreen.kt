package com.example.herbify

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope

class LoginScreen : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var cbRemember: CheckBox
    private lateinit var tvForgot: TextView
    private lateinit var tvRegister: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPrefs: SharedPreferences

    companion object {
        const val PREFS_NAME = "InstantNewsPrefs"
        const val KEY_REMEMBER = "remember_me"
        const val KEY_EMAIL = "saved_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)


        auth = FirebaseAuth.getInstance()

        sharedPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)


        initViews()
        checkRememberMe()


        btnLogin.setOnClickListener { handleLogin() }
        tvForgot.setOnClickListener { handleForgotPassword() }
        tvRegister.setOnClickListener { openRegisterScreen() }
    }



    private fun initViews() {
        etEmail    = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin   = findViewById(R.id.btnLogin)
        cbRemember = findViewById(R.id.cbRemember)
        tvForgot   = findViewById(R.id.tvForgot)
        tvRegister = findViewById(R.id.tvRegister)
    }

    private fun checkRememberMe() {
        val remembered = sharedPrefs.getBoolean(KEY_REMEMBER, false)
        if (remembered) {
            // Also check if Firebase still has an active session
            if (auth.currentUser != null) {
                goToHome()
                return
            }
            // Pre-fill email if saved
            val savedEmail = sharedPrefs.getString(KEY_EMAIL, "")
            if (!savedEmail.isNullOrEmpty()) {
                etEmail.setText(savedEmail)
                cbRemember.isChecked = true
            }
        }
    }


    private fun handleLogin() {
        val emailInput    = etEmail.text.toString().trim()
        val passwordInput = etPassword.text.toString().trim()

        // --- Input Validation ---
        if (emailInput.isEmpty()) {
            etEmail.error = "Please enter your email"
            etEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.error = "Please enter a valid email"
            etEmail.requestFocus()
            return
        }
        if (passwordInput.isEmpty()) {
            etPassword.error = "Please enter your password"
            etPassword.requestFocus()
            return
        }
        if (passwordInput.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return
        }


        setLoading(true)


        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                setLoading(false)

                if (task.isSuccessful) {
                    // Login successful
                    saveRememberMe(emailInput)
                    Toast.makeText(this, "Login successful! Welcome back 👋", Toast.LENGTH_SHORT).show()
                    goToHome()

                } else {
                    // Login failed — identify the error
                    handleLoginError(task.exception?.message, emailInput)
                }
            }
    }

    private fun handleLoginError(errorMessage: String?, email: String) {
        when {
            // User does not exist in Firebase
            errorMessage?.contains("no user record", ignoreCase = true) == true ||
                    errorMessage?.contains("user-not-found", ignoreCase = true) == true ||
                    errorMessage?.contains("There is no user", ignoreCase = true) == true -> {
                Toast.makeText(
                    this,
                    "No account found for this email. Please sign up first!",
                    Toast.LENGTH_LONG
                ).show()
                tvRegister.setTextColor(getColor(android.R.color.holo_red_light))
            }

            // Wrong password
            errorMessage?.contains("password", ignoreCase = true) == true ||
                    errorMessage?.contains("credential", ignoreCase = true) == true -> {
                etPassword.error = "Incorrect password"
                etPassword.requestFocus()
                Toast.makeText(
                    this,
                    "Incorrect password. Try again or reset your password.",
                    Toast.LENGTH_LONG
                ).show()
            }


            errorMessage?.contains("too many", ignoreCase = true) == true -> {
                Toast.makeText(
                    this,
                    "Too many failed attempts. Account temporarily locked. Reset your password.",
                    Toast.LENGTH_LONG
                ).show()
            }

            // Network error
            errorMessage?.contains("network", ignoreCase = true) == true -> {
                Toast.makeText(
                    this,
                    "Network error. Please check your internet connection.",
                    Toast.LENGTH_LONG
                ).show()
            }

            // Generic fallback
            else -> {
                Toast.makeText(
                    this,
                    "Login failed: ${errorMessage ?: "Unknown error"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun saveRememberMe(email: String) {
        val editor = sharedPrefs.edit()
        if (cbRemember.isChecked) {
            editor.putBoolean(KEY_REMEMBER, true)
            editor.putString(KEY_EMAIL, email)
        } else {
            editor.putBoolean(KEY_REMEMBER, false)
            editor.remove(KEY_EMAIL)
        }
        editor.apply()
    }


    private fun handleForgotPassword() {
        val emailInput = etEmail.text.toString().trim()

        if (emailInput.isEmpty()) {
            etEmail.error = "Enter your email above to reset password"
            etEmail.requestFocus()
            Toast.makeText(
                this,
                "Please enter your email address first",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.error = "Enter a valid email address"
            etEmail.requestFocus()
            return
        }

        setLoading(true)

        auth.sendPasswordResetEmail(emailInput)
            .addOnCompleteListener { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset email sent to $emailInput. Check your inbox!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val error = task.exception?.message
                    if (error?.contains("no user record", ignoreCase = true) == true ||
                        error?.contains("There is no user", ignoreCase = true) == true) {
                        Toast.makeText(
                            this,
                            "No account found with this email. Please register first.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to send reset email: ${error ?: "Unknown error"}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun openRegisterScreen() {
        val intent = Intent(this, SignUpScreen::class.java)
        startActivity(intent)

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }


    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setLoading(isLoading: Boolean) {
        btnLogin.isEnabled = !isLoading
        btnLogin.text = if (isLoading) "Please wait…" else "L O G I N"
    }
}