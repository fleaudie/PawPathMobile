package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class girisYap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.giris_yap)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val sifre = findViewById<EditText>(R.id.txtSifreGiris)
        val eposta = findViewById<EditText>(R.id.txtMailGiris)
        val btnGiris = findViewById<ImageButton>(R.id.imgbtnGirisYap)


        btnGiris.setOnClickListener {
            val password = sifre.text.toString()
            val mail = eposta.text.toString()
            if(mail.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Giriş başarılı
                            val user = auth.currentUser
                            if (user != null) {
                                // Kullanıcı giriş yaptı, e-posta adresi doğrulanmış mı kontrol edelim.
                                if (user.isEmailVerified) {
                                    // E-posta adresi doğrulanmışsa anaSayfa'ya yönlendir.
                                    val intent = Intent(this, anaSayfa::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        "E-posta adresinizi onaylayın.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Giriş başarısız, e-posta veya şifre yanlış olabilir.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}