package com.fleaudie.pawpath.ui

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
import com.fleaudie.pawpath.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class PetProfileEdit : AppCompatActivity() {
    private lateinit var petUid: String
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var txtEditPetName: EditText
    private lateinit var txtEditPetAge : EditText
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var imgEditPetPhoto : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_profile_edit)
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        petUid = intent.getStringExtra("petUid") ?: ""
        txtEditPetName = findViewById(R.id.txtEditPetName)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        imgEditPetPhoto = findViewById(R.id.imgEditPetPhoto)
        imgEditPetPhoto.setOnClickListener { launchGallery() }

        val btnEditPetInfo = findViewById<ImageButton>(R.id.imgbtnEditPetInfo)
        val imgEditPetPhoto = findViewById<ImageView>(R.id.imgEditPetPhoto)

        txtEditPetAge = findViewById(R.id.txtEditPetAge)
        val db = FirebaseFirestore.getInstance()

        btnEditPetInfo.setOnClickListener {
            Log.d("hata", "petid = $petUid")
           
            changePetName()
        }

        if (currentUser != null) {
            petUid = intent.getStringExtra("petUid") ?: ""

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
                                .into(imgEditPetPhoto)
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
                        if (newPetName.isNotEmpty()) {
                            petDocRef.update("petName", newPetName)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Evcil hayvan adı güncellendi", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                        if (newPetAge.isNotEmpty()){
                            petDocRef.update("petAge", newPetAge)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Evcil hayvan yaşı güncellendi", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                                Toast.makeText(this, "Evcil bilgileri güncellendi", Toast.LENGTH_SHORT).show()
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

    private fun launchGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        petUid = intent.getStringExtra("petUid") ?: ""

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            // Seçilen resmin Uri'sini al
            val selectedImageUri: Uri = data.data!!

            // Firestore'a kaydedilecek bir yol oluştur
            storageReference = FirebaseStorage.getInstance().reference
            val user = FirebaseAuth.getInstance().currentUser
            user?.uid?.let { uid ->
                val imagePath = "profilImages/$uid.jpg"
                val imageRef = storageReference!!.child(imagePath)

                // Resmi Firestore'a yükle
                imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Yükleme başarılı oldu
                        // Firestore'dan profil fotoğrafının URL'sini al
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Firestore'daki kullanıcının profil fotoğrafını güncelle
                            updateImageUrlInFirestore(imageUrl)
                        }
                    }
                    .addOnFailureListener { e ->
                        // Yükleme başarısız oldu
                        Toast.makeText(this, "Error uploading image: $e", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateImageUrlInFirestore(imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        petUid = intent.getStringExtra("petUid") ?: ""

        currentUser?.uid?.let { uid ->
            // Kullanıcının UID'sini kullanarak Firestore belgesini alın
            val userPetDocRef = db.collection("users").document(currentUser.uid).collection("userPets").document(petUid)

            // Yeni verileri hazırla
            val updatedData = hashMapOf(
                "petPhotoUrl" to imageUrl
            )
            // Firestore belgesini güncelle
            userPetDocRef
                .update(updatedData as Map<String, Any>)
                .addOnSuccessListener {
                    // Başarılı
                    Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Hata
                    Toast.makeText(this, "Error updating profile photo: $e", Toast.LENGTH_SHORT).show()
                }
        }

    }

}
