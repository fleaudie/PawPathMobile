package com.fleaudie.pawpath.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.menu.menu
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyProfile : AppCompatActivity() {
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
    }
}