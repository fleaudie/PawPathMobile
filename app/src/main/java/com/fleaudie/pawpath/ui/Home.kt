package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.NearVets
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.PetAdapterMain
import com.fleaudie.pawpath.data.UserPet
import com.fleaudie.pawpath.menu.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class Home : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var rcyUserPetMain : RecyclerView
    private lateinit var myAdapter: PetAdapterMain
    private lateinit var userPetArrayList : ArrayList<UserPet>
    private lateinit var db : FirebaseFirestore
    private lateinit var btnServPetHealth : ImageView
    private lateinit var btnServNearVet : ImageView
    private lateinit var btnServPetCare : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navMain)
        Menu.setupBottomNavigation(this, bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon_click)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        rcyUserPetMain = findViewById(R.id.rcyEvcilHayvanMain)
        userPetArrayList = arrayListOf()
        myAdapter = PetAdapterMain(userPetArrayList)
        rcyUserPetMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcyUserPetMain.setHasFixedSize(true)
        rcyUserPetMain.adapter = myAdapter
        EventChangeListener()

        btnServPetHealth = findViewById(R.id.imgServPetHealth)
        btnServNearVet = findViewById(R.id.imgServNearVet)
        btnServPetCare = findViewById(R.id.imgServPetCare)

        // Tıklama animasyonu
        btnServPetHealth.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Dokunma başladığında yukarı doğru animasyonu başlat
                    val slideUp = TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, -0.1f
                    )
                    slideUp.duration = 300
                    slideUp.fillAfter = true
                    view.startAnimation(slideUp)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Dokunma sona erdiğinde veya iptal edildiğinde aşağı doğru animasyonu başlat
                    val slideDown = TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, -0.1f,
                        Animation.RELATIVE_TO_SELF, 0f
                    )
                    slideDown.duration = 300
                    slideDown.fillAfter = true
                    view.startAnimation(slideDown)
                }
            }
            false
        }

        btnServPetCare.setOnClickListener {
            val intent = Intent(this, DailyTasks::class.java)
            startActivity(intent)
        }

        btnServNearVet.setOnClickListener {
            val intent = Intent(this, NearVets::class.java)
            startActivity(intent)
        }

        btnServPetHealth.setOnClickListener {
            val intent = Intent(this, HealthService::class.java)
            startActivity(intent)
        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null){
                            Log.e("firestore error", error.message.toString())
                            return
                        }
                        for (dc : DocumentChange in value?.documentChanges!!){
                            if (dc.type == DocumentChange.Type.ADDED){
                                userPetArrayList.add(dc.document.toObject(UserPet ::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }
    }
}