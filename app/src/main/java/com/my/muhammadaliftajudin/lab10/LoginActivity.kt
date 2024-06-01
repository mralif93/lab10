package com.my.muhammadaliftajudin.lab10

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.my.muhammadaliftajudin.lab10.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // firebase authentication
        auth = FirebaseAuth.getInstance()

        // login function
        binding.signInBtn.setOnClickListener {
            if (binding.signInEmailEditText.text.trim().toString().isNotEmpty() &&
                binding.signInpasswordEditText.text.trim().toString().isNotEmpty()) {
                loginUser(binding.signInEmailEditText.text.toString(),
                    binding.signInpasswordEditText.text.toString())
            } else {
                Snackbar.make(
                    binding.root,
                    "Please check your email or password then try again!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    fun loginUser(email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                var intent = Intent(this, ServiceActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.root,
                    "Please check your email and password.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
            .addOnFailureListener{ err ->
                Snackbar.make(
                    binding.root,
                    err.message!!,
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }
}