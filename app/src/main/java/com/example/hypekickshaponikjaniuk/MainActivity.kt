package com.example.hypekickshaponikjaniuk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekickshaponikjaniuk.databinding.ActivityMainBinding
import com.example.hypekickshaponikjaniuk.models.Sneakers
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)//włącznie binding

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //seedDatabase()

        binding.adminPanelButton.setOnClickListener {
            //startActivity(android.content.Intent(this, AdminPanelActivity::class.java)) TODO dostosować startowanie nowej aktywności do panelu Admina
        }

        binding.userPanelButton.setOnClickListener {
            //startActivity(android.content.Intent(this, StoreFrontActivity::class.java)) TODO dostosować startowanie nowej aktywności do panelu użytkownika
        }
    }
    private fun seedDatabase(){
        val sneakersList = listOf(
            Sneakers(
                brand = "Nike",
                modelName = "Air Jordan 1 Retro High OG Chicago",
                releaseYear = 2015,
                resellPrice = 1500,
                imageUrl = "https://i.postimg.cc/0NZwQsYx/Air-Jordan-1-Retro-High-OG-Chicago.jpg"
            ),
            Sneakers(
                brand = "Nike",
                modelName = "Dunk Low Panda",
                releaseYear = 2021,
                resellPrice = 500,
                imageUrl = "https://i.postimg.cc/SQVYfq3p/Dunk-Low-Panda.jpg"
            ),
            Sneakers(
                brand = "Adidas",
                modelName = "Yeezy Boost 350 V2 Zebra",
                releaseYear = 2017,
                resellPrice = 1200,
                imageUrl = "https://i.postimg.cc/HWSj7z8s/Yeezy-Boost-350-V2-Zebra.jpg"
            ),
            Sneakers(
                brand = "New Balance",
                modelName = "550 White Green",
                releaseYear = 2021,
                resellPrice = 600,
                imageUrl = "https://i.postimg.cc/43T3TFqL/550-White-Green.jpg"
            ),
            Sneakers(
                brand = "Nike",
                modelName = "Air Force 1 Low White",
                releaseYear = 2007,
                resellPrice = 400,
                imageUrl = "https://i.postimg.cc/C5tFrvbC/Air-Force-1-Low-White.jpg"
            ),
            Sneakers(
                brand = "Nike",
                modelName = "Air Max 90 Infrared",
                releaseYear = 2020,
                resellPrice = 700,
                imageUrl = "https://i.postimg.cc/rFBVSPVL/Air-Max-90-Infrared.jpg"
            ),
            Sneakers(
                brand = "Adidas",
                modelName = "UltraBoost 1.0 OG",
                releaseYear = 2015,
                resellPrice = 800,
                imageUrl = "https://i.postimg.cc/1zHy1RpN/Ultra-Boost-1-0-OG.jpg"
            ),
            Sneakers(
                brand = "ASICS",
                modelName = "Gel-Kayano 14 White Midnight",
                releaseYear = 2021,
                resellPrice = 600,
                imageUrl = "https://i.postimg.cc/dQXwYxTN/Gel-Kayano-14-White-Midnight.jpg"
            ),
            Sneakers(
                brand = "ASICS",
                modelName = "Gel-Lyte III OG",
                releaseYear = 2020,
                resellPrice = 500,
                imageUrl = "https://i.postimg.cc/fbwhC46N/Gel-Lyte-III-OG.jpg"
            ),
            Sneakers(
                brand = "Under Armour",
                modelName = "Curry 8 Flow",
                releaseYear = 2020,
                resellPrice = 550,
                imageUrl = "https://i.postimg.cc/Z0kGyX8T/Curry-8-Flow.jpg"
            )

        )


        val db = Firebase.firestore

        for(shoes in sneakersList){
            db.collection("shoes").add(shoes)
                .addOnSuccessListener {
                    Toast.makeText(this, "Database seed complete", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("FIREBASE_TEST", "Błąd podczas dodawania: ", e)
                }
        }

    }
}