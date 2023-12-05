package com.fleaudie.pawpath.menu

import android.content.Context
import android.content.Intent
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.ui.Calendar
import com.fleaudie.pawpath.ui.Home
import com.fleaudie.pawpath.ui.MyPets
import com.fleaudie.pawpath.ui.MyProfile
import com.google.android.material.bottomnavigation.BottomNavigationView

class menu {
    companion object {
        fun setupBottomNavigation(context: Context, bottomNav: BottomNavigationView) {
            bottomNav.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navMain -> {
                        if (context !is Home) {
                            val intent = Intent(context, Home::class.java)
                            context.startActivity(intent)
                        }
                        true
                    }
                    R.id.navTakvim -> {
                        if (context !is Calendar) {
                            val intent = Intent(context, Calendar::class.java)
                            context.startActivity(intent)
                        }
                        true
                    }
                    R.id.navPets -> {
                        if (context !is MyPets) {
                            val intent = Intent(context, MyPets::class.java)
                            context.startActivity(intent)
                        }
                        true
                    }
                    R.id.navProfile -> {
                        if (context !is MyProfile) {
                            val intent = Intent(context, MyProfile::class.java)
                            context.startActivity(intent)
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }
}