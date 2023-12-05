package com.fleaudie.pawpath

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class ilkGiris : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ilk_giris)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        val txtGiris = findViewById<TextView>(R.id.txtilkGiris)
        val btnKayit = findViewById<ImageButton>(R.id.btnKayit)

        btnKayit.setOnClickListener {
            intent = Intent(this , kayitOl::class.java)
            startActivity(intent)
        }

        txtGiris.setOnClickListener {
            // TextView'a tıklandığında arkaplan rengini değiştir
            it.setBackgroundResource(R.color.clickColor)

            // Görsel değişiklikleri geri almak için bir süre sonra eski haline getir
            it.postDelayed({
                it.setBackgroundResource(android.R.color.transparent)
            }, 500) // 500 milisaniye (0.5 saniye) sonra eski haline dön

            intent = Intent(this, girisYap::class.java)
            startActivity(intent)
        }
    }
}