package com.example.hypekickshaponikjaniuk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekickshaponikjaniuk.databinding.UserActivityBinding
import com.example.hypekickshaponikjaniuk.models.Sneakers
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {
    lateinit var binding: UserActivityBinding
    lateinit var shoesList: MutableList<Sneakers>
    lateinit var adapter: ShoesAdapter
    val db = FirebaseFirestore.getInstance()

    private lateinit var allShoesList: MutableList<Sneakers>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = UserActivityBinding.inflate(layoutInflater)


        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // seeDataBase()

        shoesList = mutableListOf()
        allShoesList = mutableListOf()


        adapter = ShoesAdapter(this, shoesList)
        binding.shoesGridView.adapter = adapter

        fetchDataFromCloud()

        binding.shoesGridView.setOnItemClickListener { _, _, position, _ ->
            val clickedShoe = shoesList[position]
            Toast.makeText(this, "Uruchamiam silniki w: ${clickedShoe.brand}!", Toast.LENGTH_SHORT)
                .show()
        }

        binding.shoesSearchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterShoes(newText ?: "")
                return true
            }
        })
    }

    private fun filterShoes(query: String){
        shoesList.clear()

        if(query.isEmpty()){
            shoesList.addAll(allShoesList)
        }else{
            val lowerCaseQuery = query.lowercase()

            for(shoe in allShoesList){
                if(shoe.brand.lowercase().contains(lowerCaseQuery)){
                    shoesList.add(shoe)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun fetchDataFromCloud() {

        db.collection("shoes")
            .get()
            .addOnSuccessListener { documents ->
                shoesList.clear()

                for (document in documents) {
                    val brand = document.getString("brand") ?: "Nieznany but"
                    val modelName = document.getString("modelName") ?: "Nieznany model"
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val releaseYear = document.getLong("releaseYear")?.toInt() ?: 0
                    val resellPrice = document.getLong("resellPrice")?.toInt() ?: 0

                    // Tworzymy obiekt z wyciągniętych danych i dodajemy do naszej listy
                    val shoe = Sneakers(brand, modelName, releaseYear, resellPrice, imageUrl)
                    allShoesList.add(shoe)
                }
                shoesList.clear()
                shoesList.addAll(allShoesList)

                // MAGIA: Mówimy Adapterowi "Hej, mam nowe dane! Odśwież ekran!"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE_ERROR", "Błąd pobierania danych: ", exception)
                Toast.makeText(this, "Błąd pobierania danych z chmury!", Toast.LENGTH_LONG).show()
            }
    }
}
