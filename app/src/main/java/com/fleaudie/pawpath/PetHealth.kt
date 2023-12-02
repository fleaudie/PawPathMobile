package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class PetHealth : AppCompatActivity() {

    private lateinit var txtPetName : TextView
    private lateinit var txtPetBreed : TextView
    private lateinit var btnAddHealth : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_health)

        btnAddHealth = findViewById(R.id.imgbtnAddHealth)
        txtPetName = findViewById(R.id.txtPetHealthName)
        txtPetBreed = findViewById(R.id.txtPetHealthBreed)


        txtPetName.text = intent.getStringExtra("petName")
        txtPetBreed.text = intent.getStringExtra("petBreed")

        val petUid = intent.getStringExtra("petUid") ?: ""

        btnAddHealth.setOnClickListener {
            val intent = Intent(this, PetHealthAddInfo::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }
    }
}