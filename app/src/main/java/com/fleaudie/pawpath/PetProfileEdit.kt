package com.fleaudie.pawpath

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.UUID

class PetProfileEdit : AppCompatActivity() {
    private lateinit var petUid: String
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var txtEditPetName: EditText
    private lateinit var txtEditPetAge : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_profile_edit)

        petUid = intent.getStringExtra("petUid") ?: ""
        txtEditPetName = findViewById(R.id.txtEditPetName)

        val btnEditPetInfo = findViewById<ImageButton>(R.id.imgbtnEditPetInfo)
        val imgEditPetPhoto = findViewById<ImageView>(R.id.imgEditPetPhoto)

        txtEditPetAge = findViewById(R.id.txtEditPetAge)
        val db = FirebaseFirestore.getInstance()

        btnEditPetInfo.setOnClickListener {
            Log.d("hata", "petid = $petUid")
            changePetName()
            intent = Intent(this,PetProfile::class.java)
            startActivity(intent)
        }
    }

    private fun changePetName() {
        val newPetName = txtEditPetName.text.toString()
        val newPetAge = txtEditPetAge.text.toString()
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val db = FirebaseFirestore.getInstance()
            val userPetsRef = db.collection("users").document(currentUserUid).collection("userPets")
            val petDocRef = userPetsRef.document(petUid)

            petDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        petDocRef.update("petName", newPetName)
                            petDocRef.update("petAge",newPetAge)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Evcil hayvan adı güncellendi", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Belirtilen evcil hayvan bulunamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
