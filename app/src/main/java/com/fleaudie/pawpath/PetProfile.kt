package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class PetProfile : AppCompatActivity() {
    private lateinit var txtPetName : TextView
    private lateinit var txtPetAge : TextView
    private lateinit var txtPetGender : TextView
    private lateinit var txtPetWeight : TextView
    private lateinit var txtPetBreed : TextView
    private lateinit var btnEditPetProfile : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_profile)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        btnEditPetProfile = findViewById(R.id.imgProfilePetEdit)
        txtPetName = findViewById(R.id.txtProfilePetName)
        txtPetAge = findViewById(R.id.txtProfilePetAge)
        txtPetGender = findViewById(R.id.txtProfilePetGender)
        txtPetWeight = findViewById(R.id.txtProfilePetWeight)
        txtPetBreed = findViewById(R.id.txtProfilePetBreed)


        txtPetName.text = intent.getStringExtra("petName")
        txtPetAge.text = intent.getStringExtra("petAge")
        txtPetGender.text = intent.getStringExtra("petGender")
        txtPetWeight.text = intent.getStringExtra("petWeight")
        txtPetBreed.text = intent.getStringExtra("petBreed")

        val petUid = intent.getStringExtra("petUid") ?: ""

        btnEditPetProfile.setOnClickListener{
            val intent = Intent(this, PetProfileEdit::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }
    }
}