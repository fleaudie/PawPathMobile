package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.fleaudie.pawpath.R
import com.google.firebase.auth.FirebaseAuth

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_page)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Oturum açık ise, doğrudan ana ekranına yönlendir
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        val txtGiris = findViewById<TextView>(R.id.txtilkGiris)
        val btnKayit = findViewById<ImageButton>(R.id.btnKayit)

        btnKayit.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        txtGiris.setOnClickListener {
            // TextView'a tıklandığında arkaplan rengini değiştir
            it.setBackgroundResource(R.color.clickColor)

            // Görsel değişiklikleri geri almak için bir süre sonra eski haline getir
            it.postDelayed({
                it.setBackgroundResource(android.R.color.transparent)
            }, 500) // 500 milisaniye (0.5 saniye) sonra eski haline dön

            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }
    }
}