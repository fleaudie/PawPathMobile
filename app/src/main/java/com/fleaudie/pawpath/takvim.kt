package com.fleaudie.pawpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import com.google.android.material.bottomnavigation.BottomNavigationView

class takvim : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takvim)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navTakvim)
        menu.setupBottomNavigation(this,bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon_click)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon)

        val calendarView = findViewById<CalendarView>(R.id.calTakvim)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            //Seçilen tarih değişince yapılacak eylemler
        }

        calendarView.minDate = System.currentTimeMillis() // Minimum tarih olarak şu anki tarihi ayarlar
        calendarView.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7) // Maksimum tarihi bir hafta sonraya ayarlar

        val selectedDate = calendarView.date
        calendarView.date = System.currentTimeMillis() // Takvimde seçili tarihi şu anki tarih olarak ayarlar

    }
}