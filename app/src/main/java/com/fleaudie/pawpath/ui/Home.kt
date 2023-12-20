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
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.PetAdapterMain
import com.fleaudie.pawpath.adapter.UpcomingEventsAdapter
import com.fleaudie.pawpath.data.EventModel
import com.fleaudie.pawpath.data.UserPet
import com.fleaudie.pawpath.menu.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar

class Home : AppCompatActivity() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var rcyUserPetMain : RecyclerView
    private lateinit var myAdapter: PetAdapterMain
    private lateinit var userPetArrayList : ArrayList<UserPet>
    private lateinit var db : FirebaseFirestore
    private lateinit var btnServPetHealth : ImageView
    private lateinit var btnServPetCare : ImageView
    private lateinit var rcyUpcomingEvents : RecyclerView
    private lateinit var upcomingEventsAdapter : UpcomingEventsAdapter
    private lateinit var upcomingEventsList : ArrayList<EventModel>
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

        rcyUpcomingEvents = findViewById(R.id.rcyUpcomingEvents)
        upcomingEventsList = arrayListOf()
        upcomingEventsAdapter = UpcomingEventsAdapter(upcomingEventsList)
        rcyUpcomingEvents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyUpcomingEvents.setHasFixedSize(true)
        rcyUpcomingEvents.adapter = upcomingEventsAdapter
        showUpcomingEventsForCurrentDate()

        btnServPetHealth = findViewById(R.id.imgServPetHealth)
        btnServPetCare = findViewById(R.id.imgServPetCare)

        btnServPetCare.setOnTouchListener { view, event ->
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

    private fun showUpcomingEventsForCurrentDate() {
        // Bu kısımda mevcut tarihe göre günlük planları kontrol edip, eventListArrayList içeriğini güncelleyin
        upcomingEventsList.clear()  // Mevcut etkinlikleri temizle

        if (currentUser != null) {
            val currentDate = getCurrentDate()  // Mevcut tarihi al

            val eventDocRef =
                db.collection("users").document(currentUser.uid).collection("events")
                    .document(currentDate)

            // dailyEvents koleksiyonunu kontrol et
            eventDocRef.collection("dailyEvents")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val upcomingEvents = mutableListOf<EventModel>()

                    for (document in querySnapshot.documents) {
                        // Her belgeyi al ve EventModel'e çevirip listeye ekle
                        val event = document.getString("eventName")
                        val eventTime = document.getString("eventTime")

                        if (event != null && eventTime != null) {
                            val eventModel = EventModel(event, eventTime)
                            upcomingEvents.add(eventModel)
                        }
                    }

                    // Yaklaşan 2 etkinliği al
                    val upcomingEventsToShow = upcomingEvents.take(2)

                    // RecyclerView'a eklenen yaklaşan etkinlikleri ekle
                    upcomingEventsList.addAll(upcomingEventsToShow)

                    // RecyclerView'ı güncelle
                    upcomingEventsAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda
                    // Hata durumu hakkında bir şey yapmanız gerekiyorsa buraya ekleyebilirsiniz.
                    Log.e("Firestore Error", exception.message.toString())
                }
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1 // Ay, 0'dan başlar
        val year = calendar.get(Calendar.YEAR)
        return "$day/${month}/$year"
    }

}