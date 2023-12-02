package com.fleaudie.pawpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PetHealthAddInfo : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnAddHealthInfo : ImageButton
    private lateinit var txtAllergyName : EditText
    private lateinit var txtAllergyInfo : EditText
    private lateinit var spnVaccineName : Spinner
    private lateinit var spnVaccineDate : Spinner
    private lateinit var spnDiseaseName : Spinner
    private lateinit var rcyUserPets : RecyclerView
    private lateinit var myAdapter: petNameAdapter
    private lateinit var userPetArrayList : ArrayList<userPet>
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_health_add_info)
    }
}