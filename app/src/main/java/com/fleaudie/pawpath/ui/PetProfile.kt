package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fleaudie.pawpath.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class PetProfile : AppCompatActivity() {
    private lateinit var txtPetName : TextView
    private lateinit var txtPetAge : TextView
    private lateinit var txtPetGender : TextView
    private lateinit var txtPetWeight : TextView
    private lateinit var txtPetBreed : TextView
    private lateinit var btnEditPetProfile : ImageView
    private lateinit var imgProfilePetPhoto : CircleImageView
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var db : FirebaseFirestore
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
        imgProfilePetPhoto = findViewById(R.id.imgProfilePetPhoto)

        val petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()
        if (currentUser != null) {
            val userPetDocRef = db.collection("users").document(currentUser.uid).collection("userPets").document(petUid)

            userPetDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Firestore belgesi varsa, profilFotoURL alanını al
                        val petPhotoUrl = documentSnapshot.getString("petPhotoUrl")

                        // Şimdi Glide ile ImageView'a resmi yükle
                        if (petPhotoUrl != null) {
                            Glide.with(this)
                                .load(petPhotoUrl)
                                .into(imgProfilePetPhoto)
                        }
                    } else {
                        // Firestore belgesi yoksa, kullanıcıya bilgi ver
                        Toast.makeText(this, "Profil fotoğrafı bulunamadı.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Firestore'dan belge çekme sırasında bir hata oluştu
                    Toast.makeText(this, "Hata: $exception", Toast.LENGTH_SHORT).show()
                }
        }

        txtPetName.text = intent.getStringExtra("petName")
        txtPetAge.text = intent.getStringExtra("petAge")
        txtPetGender.text = intent.getStringExtra("petGender")
        txtPetWeight.text = intent.getStringExtra("petWeight")
        txtPetBreed.text = intent.getStringExtra("petBreed")

        btnEditPetProfile.setOnClickListener{
            val intent = Intent(this, PetProfileEdit::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }
    }
}