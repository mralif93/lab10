package com.my.muhammadaliftajudin.lab10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.my.muhammadaliftajudin.lab10.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Firebase connection authentication from app
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        // Button Listener
        binding.createBtn.setOnClickListener {
            if (binding.signUpEmailEditText.text.trim().toString().isNotEmpty() &&
                binding.signUpPasswordEditText.text.trim().toString().isNotEmpty()) {
                createUser(
                    binding.signUpEmailEditText.text.toString(),
                    binding.signUpPasswordEditText.text.toString()
                )
            } else {
                Snackbar.make(
                    binding.root,
                    "Please check your email or password then try again!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    fun createUser(email:String, password:String) {
        // create user using email and password
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendData(
                    binding.fullnameEditText.text.trim().toString(),
                    binding.cityEditText.text.trim().toString(),
                    binding.countryEditText.text.trim().toString(),
                    binding.phoneEditText.text.trim().toString()
                )
            } else {
                Snackbar.make(
                    binding.root,
                    "Enter a valid enter email and password",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun sendData(name:String, city:String, country:String, phone: String) {
        val customer = hashMapOf(
            "name" to name,
            "city" to city,
            "country" to country,
            "phone" to phone,
        )

        if (name.isNotEmpty() && city.isNotEmpty() && country.isNotEmpty() && phone.isNotEmpty()) {
            db.collection("customers")
                .add(customer)
                .addOnSuccessListener{ documentReference ->
                    Log.d("debug","Document successfully added with id ${documentReference.id}")

                    val intent = Intent(this, ThanksActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { err ->
                    Log.d("debug", "An error happen ${err.message}")
                }
        } else {
            Snackbar.make(
                binding.root,
                "Please check your input then try again!",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}

