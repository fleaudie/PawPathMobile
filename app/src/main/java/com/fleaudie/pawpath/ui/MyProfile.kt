package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.menu.menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class MyProfile : AppCompatActivity() {

    private lateinit var btnLogOut : ConstraintLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var btnEditProfile: ConstraintLayout
    private lateinit var txtUserName: TextView
    private lateinit var imgProfilePhoto : CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navProfile)
        menu.setupBottomNavigation(this, bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon_click)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        imgProfilePhoto = findViewById(R.id.imgProfilePhoto)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Kullanıcı bilgilerini gösterecek TextView'leri tanımlayın
        txtUserName = findViewById(R.id.txtUserName)
        //txtUserSurname = findViewById(R.id.txtUserSurname)
        // Diğer TextView'leri burada tanımlayın.

        // Kullanıcının UID'sini alın
        val currentUserUid = auth.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        if (currentUserUid != null) {
            val userPetDocRef = db.collection("users").document(currentUserUid)

            userPetDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Firestore belgesi varsa, profilFotoURL alanını al
                        val petPhotoUrl = documentSnapshot.getString("userPhotoUrl")

                        // Şimdi Glide ile ImageView'a resmi yükle
                        if (petPhotoUrl != null) {
                            Glide.with(this)
                                .load(petPhotoUrl)
                                .into(imgProfilePhoto)
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

        // Firestore'dan kullanıcı bilgilerini çekmek için sorguyu hazırlayın
        if (currentUserUid != null) {
            val userDocument = firestore.collection("users").document(currentUserUid)

            // Firestore sorgusunu gerçekleştirin
            userDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Firestore'da belirtilen belge bulunduğunda
                        val userName = document.getString("İsim")
                        val userSurname = document.getString("Soyisim")
                        val userNameSurname = userName + " " + userSurname

                        // TextView'lere kullanıcı bilgilerini yerleştirin
                        txtUserName.text = userNameSurname
                        // Diğer TextView'leri doldurun.

                    } else {
                        // Firestore'da belirtilen belge bulunamadığında
                        // Kullanıcı bilgileri mevcut değilse gerekli işlemleri yapabilirsiniz.
                    }
                }
                .addOnFailureListener { exception ->
                    // Firestore sorgusu başarısız olduğunda hata işleme ekleyin
                    // Hata mesajını loglayabilir veya kullanıcıya bir hata mesajı gösterebilirsiniz.
                }
        }

        btnEditProfile = findViewById(R.id.consEditProfile)
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, MyProfileEdit::class.java)
            startActivity(intent)
        }

        btnLogOut = findViewById(R.id.consLogOut)
        btnLogOut.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Çıkış Onayı")
            alertDialogBuilder.setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")

            // "Evet" butonu
            alertDialogBuilder.setPositiveButton("Evet") { _,_  ->
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                finishAffinity()
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