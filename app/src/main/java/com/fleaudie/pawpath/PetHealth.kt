package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class PetHealth : AppCompatActivity() {

    private lateinit var txtPetName : TextView
    private lateinit var txtPetBreed : TextView
    private lateinit var btnAddHealth : ImageButton
    private lateinit var db : FirebaseFirestore
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var rcyPetHealthAllergy : RecyclerView
    private lateinit var allergyAdapter: allergyAdapter
    private lateinit var userPetArrayList : ArrayList<userPet>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_health)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

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

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null){
                            Log.e("firestore error", error.message.toString())
                            return
                        }
                        for (dc : DocumentChange in value?.documentChanges!!){
                            if (dc.type == DocumentChange.Type.ADDED){
                                userPetArrayList.add(dc.document.toObject(userPet ::class.java))
                            }
                        }
                        allergyAdapter.notifyDataSetChanged()
                    }

                })
        }
    }
}