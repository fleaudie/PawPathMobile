package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.fleaudie.pawpath.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singup_activity)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        auth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        val btnKayit = findViewById<ImageButton>(R.id.imgbtnKayitOl)
        val isim = findViewById<EditText>(R.id.txtIsim)
        val soyisim = findViewById<EditText>(R.id.txtSoyisim)
        val email = findViewById<EditText>(R.id.txtMail)
        val sifre = findViewById<EditText>(R.id.txtSifre)
        val sifreOnay = findViewById<EditText>(R.id.txtSifreOnay)

        btnKayit.setOnClickListener {
            val mail = email.text.toString()
            val password = sifre.text.toString()
            val confPassword = sifreOnay.text.toString()
            val name = isim.text.toString()
            val surname = soyisim.text.toString()

            if(mail.isNotEmpty() && password.isNotEmpty() && confPassword.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()){
                if ( password == confPassword) {
                    auth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Kayıt başarılı
                                val user = auth.currentUser
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Kayıt Başarılı. E-posta doğrulama gönderildi.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val userDocument = firestore.collection("users").document(user?.uid!!)
                                            userDocument.set(mapOf(

                                                "İsim" to name,
                                                "Soyisim" to surname,
                                                "Mail adresi" to mail ))

                                            val intent = Intent(this, Login::class.java)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Kayıt Başarılı, ancak e-posta doğrulama gönderilemedi.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                finish()
                            } else {
                                // Kayıt başarısız
                                Toast.makeText(
                                    this,
                                    "Kayıt başarısız, lütfen tekrar deneyin.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Şifreler aynı değil.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Lütfen eksik bilgileri doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}