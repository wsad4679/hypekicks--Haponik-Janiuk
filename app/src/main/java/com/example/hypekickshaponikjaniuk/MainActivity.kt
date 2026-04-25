package com.example.hypekickshaponikjaniuk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekickshaponikjaniuk.databinding.ActivityMainBinding

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


        binding.adminPanelButton.setOnClickListener {
            //startActivity(android.content.Intent(this, AdminPanelActivity::class.java)) TODO dostosować startowanie nowej aktywności do panelu Admina
        }

        binding.userPanelButton.setOnClickListener {
            //startActivity(android.content.Intent(this, StoreFrontActivity::class.java)) TODO dostosować startowanie nowej aktywności do panelu użytkownika
        }
    }
}