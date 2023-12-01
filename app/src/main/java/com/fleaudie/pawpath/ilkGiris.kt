package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class ilkGiris : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ilk_giris)

        val txtGiris = findViewById<TextView>(R.id.txtilkGiris)
        val btnKayit = findViewById<ImageButton>(R.id.btnKayit)

        btnKayit.setOnClickListener {
            intent = Intent(this , kayitOl::class.java)
            startActivity(intent)
        }
        txtGiris.setOnClickListener {
            intent = Intent(this, girisYap::class.java)
            startActivity(intent)
        }
    }
}