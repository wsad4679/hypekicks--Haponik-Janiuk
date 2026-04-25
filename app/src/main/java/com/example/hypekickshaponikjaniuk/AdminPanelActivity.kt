package com.example.hypekickshaponikjaniuk

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekickshaponikjaniuk.databinding.ActivityAdminPanelBinding
import com.example.hypekickshaponikjaniuk.models.Sneakers
import com.google.firebase.firestore.FirebaseFirestore

class AdminPanelActivity : AppCompatActivity() {
    lateinit var shoesNameList: MutableList<String>
    lateinit var shoesIdList: MutableList<String>
    lateinit var adapter: ArrayAdapter<String>
    val db = FirebaseFirestore.getInstance() // łączenie się z bazą danych fireStore
    private lateinit var binding: ActivityAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        shoesNameList = mutableListOf()// lista z nazwami butów do ListView
        shoesIdList = mutableListOf() //lista z id do usuwania i aktualizowania butów
        fetchDataFromCloud()

        val shoeListView = binding.shoesListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoesNameList)
        shoeListView.adapter = adapter

        binding.addToDatabaseButton.setOnClickListener { // dodawnie danych do bazy fireStore

            val brand = binding.brandEditText.text.toString()
            val model = binding.modelEditText.text.toString()
            val releaseYear: Int = binding.releaseYearEditText.text.toString().toInt()
            val resellPrice: Int = binding.resellPriceEditText.text.toString().toInt()
            val imageUrl = binding.imageUrlEditText.text.toString()


            db.collection("shoes").add(
                Sneakers(
                    brand, model, releaseYear, resellPrice, imageUrl
                )
            ).addOnSuccessListener {
                binding.brandEditText.text.clear() // czyszczenie formularza
                binding.modelEditText.text.clear()
                binding.releaseYearEditText.text.clear()
                binding.resellPriceEditText.text.clear()
                binding.imageUrlEditText.text.clear()
                Toast.makeText(this, "Successfully added item", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error while adding item", Toast.LENGTH_SHORT).show()
            }


        }



        shoeListView.setOnItemLongClickListener{_, _, position, _ -> // funckja do usuwania przedmiotu z bazy danych
            db.collection("shoes").document(shoesIdList[position]).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Succesfully deleted item", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Unexpected error occurd", Toast.LENGTH_SHORT).show()
                }
            true // musi być aby funkcja działała
        }


        shoeListView.setOnItemClickListener{_,_, position, _ ->
            db.collection("shoes").document(shoesIdList[position]).update("resellPrice", binding.priceUpdateEditText.text.toString().toInt())
                .addOnSuccessListener {
                    Toast.makeText(this, "Succesfully updated item price", Toast.LENGTH_SHORT).show()
                    binding.priceUpdateEditText.text.clear()
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Błąd aktualizacji", e)

                }
        }

    }

        private fun fetchDataFromCloud() {
        db.collection("shoes")
            .addSnapshotListener { snapshot, e -> // zamiast get jest snapshot aby nie informować cały czas o zmianach w bazie tylko automatycvznie nasłuchuje
                if (e != null) {
                    Log.w("firebase", "Błąd subskrypcji", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {

                    shoesNameList.clear()
                    shoesIdList.clear()

                    for (document in snapshot) {
                        val shoeName = document.getString("modelName") ?: "Uknown shoe model name"
                        val shoePrice = document.getLong("resellPrice") ?: -1
                        shoesIdList.add(document.id)// pobranie id do usuwania przedmiotów
                        shoesNameList.add("$shoeName   $shoePrice $")
                    }
                    adapter.notifyDataSetChanged()

                }
            }


    }
}