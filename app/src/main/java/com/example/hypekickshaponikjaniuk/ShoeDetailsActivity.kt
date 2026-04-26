package com.example.hypekickshaponikjaniuk

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.hypekickshaponikjaniuk.databinding.ActivityShoeDetailsBinding
import com.example.hypekickshaponikjaniuk.models.Sneakers

class ShoeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityShoeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //odbiór danych
        val shoe = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            intent.getSerializableExtra("SHOE_DATA", Sneakers::class.java)
        }else{
            @Suppress("DEPRACATION") //kompilator ma podobno nie krzyczec dzeiki temu
            intent.getSerializableExtra("SHOE_DATA") as? Sneakers
        }
        //sprawdzamy czy dotarlo
        if(shoe != null){
            binding.detailBrandTextView.text = shoe.brand
            binding.detailModelTextView.text = shoe.modelName
            binding.detailReleaseYearTextView.text = "Rok wydania: ${shoe.releaseYear}"
            binding.detailResellPriceTextView.text = "Cena odsprzedaży: ${shoe.reselPrice}"

            Glide.with(this).load(shoe.imageUrl)
                .placeholder(R.mipmap.ic_launcher) //jakby nie wczytalo zdjecia
                .into(binding.detailImageView)

            binding.backButton.setOnClickListener {
                finish()
            }
        }else{
            Toast.makeText(
                this, "Błąd ładowanina zdjęcia",
                Toast.LENGTH_LONG).show()
            finish()
        }


    }
}