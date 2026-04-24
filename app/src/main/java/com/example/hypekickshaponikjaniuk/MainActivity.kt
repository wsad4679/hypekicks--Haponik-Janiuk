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
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var shoesList: MutableList<Sneakers>
    lateinit var adapter: ShoesAdapter
    val db = FirebaseFirestore.getInstance()

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
        shoesList = mutableListOf()

        adapter = ShoesAdapter(this, shoesList)
        binding.shoesGridView.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        fetchDataFromCloud()

        binding.shoesGridView.setOnItemClickListener{_, _, position, _ ->
            val clickedShoe = shoesList[position]
            Toast.makeText(this, "Kliknięty but: ${clickedShoe.brand}.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchDataFromCloud(){
        db.collection("shoes")
            .get()
            .addOnSuccessListener { documents ->
                shoesList.clear()

                for(document in documents){
                    val brand = document.getString("brand") ?: "Nieznany but"
                    val modelName = document.getString("modelName") ?: "Nieznany model"
                    val releaseYear = document.getLong("releaseYear")?.toInt() ?: 0
                    val resellPrice = document.getLong("resellPrice")?.toInt() ?: 0
                    val imageUrl = document.getString("imageUrl") ?: ""

                    val shoe = Sneakers(brand, modelName, releaseYear, resellPrice)
                    shoesList.add(shoe)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Błąd pobierania danych z chmury!", Toast.LENGTH_LONG).show()
            }
    }
}





















