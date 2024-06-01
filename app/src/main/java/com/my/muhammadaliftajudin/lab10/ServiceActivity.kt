package com.my.muhammadaliftajudin.lab10

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.my.muhammadaliftajudin.lab10.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityServiceBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // firebase authentication
        auth = FirebaseAuth.getInstance()

        // firestore connection
        db = Firebase.firestore

        // read collections
        readFireStoreData()

        // button action
        binding.signOutBtn.setOnClickListener {
            auth.signOut()

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun readFireStoreData() {
        db.collection("customers")
            .get()
            .addOnCompleteListener {
                val result:StringBuffer= StringBuffer()
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val name = document.get("name") ?: "N/A"
                        val city = document.get("city") ?: "N/A"
                        val country = document.get("country") ?: "N/A"
                        val phone = document.get("phone") ?: "N/A"

                        // display result
                        result.append("Name: ").append(name).append("\n")
                            .append("City: ").append(city).append("\n")
                            .append("Country: ").append(country).append("\n")
                            .append("Phone: ").append(phone).append("\n").append("\n").append("\n")

                        binding.resultText.text = result
                    }
                }
            }
    }
}