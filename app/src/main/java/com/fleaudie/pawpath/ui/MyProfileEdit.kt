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

class MyProfileEdit : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var txtEditName: EditText
    private lateinit var txtEditSurname : EditText
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var imgEditPhoto : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile_edit)
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        txtEditName = findViewById(R.id.txtEditName)
        txtEditSurname = findViewById(R.id.txtEditSurname)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        imgEditPhoto = findViewById(R.id.imgEditPhoto)
        imgEditPhoto.setOnClickListener { launchGallery() }

        val btnEditInfo = findViewById<ImageButton>(R.id.imgbtnEditInfo)

        val db = FirebaseFirestore.getInstance()

        btnEditInfo.setOnClickListener {
            changeName()
        }

        if (currentUser != null) {
            val userDocRef = db.collection("users").document(currentUser.uid)

            userDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Firestore belgesi varsa, profilFotoURL alanını al
                        val userPhotoUrl = documentSnapshot.getString("userPhotoUrl")

                        // Şimdi Glide ile ImageView'a resmi yükle
                        if (userPhotoUrl != null) {
                            Glide.with(this)
                                .load(userPhotoUrl)
                                .into(imgEditPhoto)
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

    private fun changeName() {
        val newName = txtEditName.text.toString()
        val newSurname = txtEditSurname.text.toString()
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users")
            val userDocRef = userRef.document(currentUserUid)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        if (newName.isNotEmpty()) {
                            userDocRef.update("İsim", newName)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "İsminiz güncellendi.", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                                }
                        }

                        if (newSurname.isNotEmpty()) {
                            userDocRef.update("Soyisim", newSurname)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Soyisiminiz güncellendi.", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Hata: $e", Toast.LENGTH_SHORT).show()
                                }
                        }
                        else {
                            Toast.makeText(this, "Bilgileriniz güncellendi", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Bir hata oluştu.", Toast.LENGTH_SHORT).show()
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

        currentUser?.uid?.let { uid ->
            // Kullanıcının UID'sini kullanarak Firestore belgesini alın
            val userDocRef = db.collection("users").document(currentUser.uid)

            // Yeni verileri hazırla
            val updatedData = hashMapOf(
                "userPhotoUrl" to imageUrl
            )
            // Firestore belgesini güncelle
            userDocRef
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
