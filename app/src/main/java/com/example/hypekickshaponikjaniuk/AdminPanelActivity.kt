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
import com.google.firebase.firestore.FirebaseFirestore

class AdminPanelActivity : AppCompatActivity() {
    lateinit var shoesNameList: MutableList<String>
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

        fetchDataFromCloud()

        val shoeListView = binding.shoesListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoesNameList)
        shoeListView.adapter = adapter

        binding.addToDatabaseButton.setOnClickListener { // dodawnie danych do bazy fireStore

        }

    }

        private fun fetchDataFromCloud() {
        db.collection("shoes")
            .get()
            .addOnSuccessListener { documents ->
                shoesNameList.clear()

                for(document in documents){
                    val shoeName = document.getString("modelName")?: "Uknown shoe model name"
                    val shoePrice = document.getLong("resellPrice")?: -1

                    shoesNameList.add("$shoeName   $shoePrice $")
                }
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Error while loading data!", Toast.LENGTH_LONG).show()
            }

    }
}