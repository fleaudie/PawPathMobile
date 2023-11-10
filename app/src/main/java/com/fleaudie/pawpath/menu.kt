package com.fleaudie.pawpath

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

class menu {
    companion object {
        fun setupBottomNavigation(context: Context, bottomNav: BottomNavigationView) {
            bottomNav.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navMain -> {
                        val intent = Intent(context, anaSayfa::class.java)
                        context.startActivity(intent)
                        true
                    }
                    R.id.navTakvim -> {
                        val intent = Intent(context, takvim::class.java)
                        context.startActivity(intent)
                        true
                    }
                    R.id.navPets -> {
                        val intent = Intent(context, evcilHayvanlarim::class.java)
                        context.startActivity(intent)
                        true
                    }
                    R.id.navProfile -> {
                        val intent = Intent(context, myProfile::class.java)
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}