package com.example.herbify


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpScreen : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var cbTerms: CheckBox
    private lateinit var btnSignUp: MaterialButton
    private lateinit var tvLogin: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize all views
        initViews()

        // Set click listeners
        btnSignUp.setOnClickListener { handleSignUp() }
        tvLogin.setOnClickListener { goBackToLogin() }
    }

    private fun initViews() {
        etName              = findViewById(R.id.etName)
        etEmail             = findViewById(R.id.etEmail)
        etPassword          = findViewById(R.id.etPassword)
        etConfirmPassword   = findViewById(R.id.etConfirmPassword)
        nameLayout          = findViewById(R.id.nameLayout)
        emailLayout         = findViewById(R.id.emailLayout)
        passwordLayout      = findViewById(R.id.passwordLayout)
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout)
        cbTerms             = findViewById(R.id.cbTerms)
        btnSignUp           = findViewById(R.id.btnSignUp)
        tvLogin             = findViewById(R.id.tvLogin)
    }


    private fun handleSignUp() {
        // Clear any old errors first
        clearErrors()

        val name            = etName.text.toString().trim()
        val email           = etEmail.text.toString().trim()
        val password        = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()


        if (!validateInputs(name, email, password, confirmPassword)) return


        setLoading(true)

        // Create the Firebase user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account created — now save the display name
                    saveDisplayName(name)
                } else {
                    setLoading(false)
                    handleSignUpError(task.exception?.message)
                }
            }
    }


    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {


        if (name.isEmpty()) {
            nameLayout.error = "Please enter your full name"
            etName.requestFocus()
            return false
        }
        if (name.length < 2) {
            nameLayout.error = "Name must be at least 2 characters"
            etName.requestFocus()
            return false
        }

        // Email
        if (email.isEmpty()) {
            emailLayout.error = "Please enter your email"
            etEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Please enter a valid email address"
            etEmail.requestFocus()
            return false
        }

        // Password
        if (password.isEmpty()) {
            passwordLayout.error = "Please enter a password"
            etPassword.requestFocus()
            return false
        }
        if (password.length < 6) {
            passwordLayout.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return false
        }
        if (!isPasswordStrong(password)) {
            passwordLayout.error = "Password must contain a letter and a number"
            etPassword.requestFocus()
            return false
        }

        // Confirm password
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.error = "Please confirm your password"
            etConfirmPassword.requestFocus()
            return false
        }
        if (password != confirmPassword) {
            confirmPasswordLayout.error = "Passwords do not match"
            etConfirmPassword.requestFocus()
            return false
        }


        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun isPasswordStrong(password: String): Boolean {
        val hasLetter = password.any { it.isLetter() }
        val hasDigit  = password.any { it.isDigit() }
        return hasLetter && hasDigit
    }


    private fun saveDisplayName(name: String) {
        val user = auth.currentUser ?: return

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { profileTask ->
                if (profileTask.isSuccessful) {
                    // Profile saved — now send email verification
                    sendVerificationEmail()
                } else {
                    // Profile update failed but account was created — still proceed
                    setLoading(false)
                    Toast.makeText(this, "Account created! (Could not save name)", Toast.LENGTH_SHORT).show()
                    sendVerificationEmail()
                }
            }
    }


    private fun sendVerificationEmail() {
        val user = auth.currentUser ?: return

        user.sendEmailVerification()
            .addOnCompleteListener { verifyTask ->
                setLoading(false)
                if (verifyTask.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Account created! A verification email has been sent to ${user.email}. Please verify before logging in.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Account created! (Verification email could not be sent. You can resend it from settings.)",
                        Toast.LENGTH_LONG
                    ).show()
                }
                auth.signOut()
                goBackToLogin()
            }
    }


    private fun handleSignUpError(errorMessage: String?) {
        when {
            // Email already in use
            errorMessage?.contains("email address is already in use", ignoreCase = true) == true ||
                    errorMessage?.contains("email-already-in-use", ignoreCase = true) == true -> {
                emailLayout.error = "This email is already registered"
                etEmail.requestFocus()
                Toast.makeText(
                    this,
                    "An account with this email already exists. Try logging in instead.",
                    Toast.LENGTH_LONG
                ).show()
            }

            errorMessage?.contains("badly formatted", ignoreCase = true) == true -> {
                emailLayout.error = "Invalid email format"
                etEmail.requestFocus()
            }


            errorMessage?.contains("weak-password", ignoreCase = true) == true ||
                    errorMessage?.contains("password is too weak", ignoreCase = true) == true -> {
                passwordLayout.error = "Password is too weak. Use at least 6 characters."
                etPassword.requestFocus()
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
                    "Sign up failed: ${errorMessage ?: "Unknown error"}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun clearErrors() {
        nameLayout.error            = null
        emailLayout.error           = null
        passwordLayout.error        = null
        confirmPasswordLayout.error = null
    }
    private fun setLoading(isLoading: Boolean) {
        btnSignUp.isEnabled = !isLoading
        btnSignUp.text = if (isLoading) "Creating Account…" else "C R E A T E  A C C O U N T"
    }

    private fun goBackToLogin() {
        finish() // Just pop this activity off the back stack
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}

