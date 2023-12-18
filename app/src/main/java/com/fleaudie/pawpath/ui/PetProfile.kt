package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var btnRemovePet : Button
    private lateinit var btnGoHealthInfo : Button
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
        btnRemovePet = findViewById(R.id.btnRemovePet)
        btnGoHealthInfo = findViewById(R.id.btnGoHealthInfo)

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
                        txtPetName.text = documentSnapshot.getString("petName")
                        txtPetAge.text = documentSnapshot.getString("petAge")
                        txtPetGender.text = documentSnapshot.getString("petGender")
                        txtPetWeight.text = documentSnapshot.getString("petWeight")
                        txtPetBreed.text =documentSnapshot.getString("petBreed")
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
        else{
            txtPetName.text = intent.getStringExtra("petName")
            txtPetAge.text = intent.getStringExtra("petAge")
            txtPetGender.text = intent.getStringExtra("petGender")
            txtPetWeight.text = intent.getStringExtra("petWeight")
            txtPetBreed.text = intent.getStringExtra("petBreed")
        }



        btnEditPetProfile.setOnClickListener{
            val intent = Intent(this, PetProfileEdit::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }

        btnGoHealthInfo.setOnClickListener {
            val intent = Intent(this, PetHealth::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }

        btnRemovePet.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Uyarı!")
            alertDialogBuilder.setMessage("Evcil hayvanı kaldırmak istediğinize emin misiniz?")

            // "Evet" butonu
            alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
                if (currentUser != null) {
                    val userPetDocRef =
                        db.collection("users").document(currentUser.uid).collection("userPets")
                            .document(petUid)

                    userPetDocRef.delete()
                        .addOnSuccessListener {
                            // Delete the pet document successfully, now delete associated data if any
                            // You can delete other related data/documents here

                            // Display a success message
                            Toast.makeText(
                                this,
                                "Evcil hayvan başarıyla kaldırıldı.",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Redirect the user to another activity or perform any other necessary actions
                            finish() // Close the current activity or redirect as needed
                        }
                        .addOnFailureListener { exception ->
                            // There was an error while deleting the pet document
                            Toast.makeText(this, "Bir hata oluştu: $exception", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            // "Hayır" butonu
            alertDialogBuilder.setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}