package com.fleaudie.pawpath

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView

class anaSayfa : AppCompatActivity() {
    private lateinit var rcyUserPetMain : RecyclerView
    private lateinit var myAdapter: petAdapterMain
    private lateinit var userPetArrayList : ArrayList<userPet>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ana_sayfa)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navMain)
        menu.setupBottomNavigation(this,bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon_click)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon)
    }
}