package com.fleaudie.pawpath.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.EventAdapter
import com.fleaudie.pawpath.data.EventModel
import com.fleaudie.pawpath.menu.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Calendar : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var calendarView: CalendarView
    private val db = FirebaseFirestore.getInstance()
    private lateinit var txtAddEvent : EditText
    private lateinit var btnAddEvent : Button
    private lateinit var txtAddEventTime : EditText
    private lateinit var eventListArrayList : ArrayList<EventModel>
    private lateinit var rcyEvents: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navTakvim)
        Menu.setupBottomNavigation(this, bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon_click)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        eventListArrayList = ArrayList()

        txtAddEventTime = findViewById(R.id.txtAddEventTime)
        btnAddEvent = findViewById(R.id.btnAddEvent)
        txtAddEvent = findViewById(R.id.txtAddEvent)
        calendarView = findViewById(R.id.calendarView)
        rcyEvents = findViewById(R.id.rcyEvent)
        eventAdapter = EventAdapter(eventListArrayList)
        rcyEvents.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyEvents.setHasFixedSize(true)
        rcyEvents.adapter = eventAdapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Log.d("Selected Date", selectedDate)

            showEventForDate(selectedDate)

            btnAddEvent.setOnClickListener {
                val eventText = txtAddEvent.text.toString()
                val eventTime = txtAddEventTime.text.toString()

                if (selectedDate.isNotEmpty() && eventText.isNotEmpty() && eventTime.isNotEmpty()) {
                    // Kullanıcının girdiği metni RecyclerView'e ekle
                    // Kullanıcının girdiği metni Firestore'a kaydet
                    addEventForDate(selectedDate, eventText, eventTime)

                    // Etkinlikleri göster
                    showEventForDate(selectedDate)
                }

                // Ekledikten sonra EditText'leri temizle
                txtAddEvent.text.clear()
                txtAddEventTime.text.clear()
            }
        }

    }

    private fun addEventForDate(selectedDate: String, event: String, eventTime: String) {
        // Firestore'a günlük plan eklemek için kullanıcının UID'sini kullan
        val currentUserUid = currentUser?.uid
        if (currentUserUid != null) {
            val eventDocRef =
                db.collection("users").document(currentUserUid).collection("events")
                    .document(selectedDate)
                    .collection("dailyEvents")

            // Eğer belirli bir tarih için belirli bir etkinlik varsa, koleksiyona yeni etkinliği ekleyin
            val newEventModel = hashMapOf(
                "eventName" to event,
                "eventTime" to eventTime
            )

            eventDocRef
                .add(newEventModel)
                .addOnSuccessListener { documentReference ->
                    // Yeni etkinliği ekledikten sonra güncellenmiş etkinlikleri göster
                    // Burada eklenen etkinliği manuel olarak listeye ekleyebiliriz
                    Log.d("Firestore", "Event added successfully.")
                    Log.d("Selected Date", selectedDate)
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda
                    // Hata durumu hakkında bir şey yapmanız gerekiyorsa buraya ekleyebilirsiniz.
                    Log.e("Firestore Error", exception.message.toString())
                }
        }
    }

    private fun showEventForDate(selectedDate: String) {
        // Bu kısımda seçilen tarihe göre günlük planları kontrol edip, eventListArrayList içeriğini güncelleyin
        eventListArrayList.clear()  // Mevcut etkinlikleri temizle

        if (currentUser != null) {
            val eventDocRef =
                db.collection("users").document(currentUser.uid).collection("events")
                    .document(selectedDate)

            // dailyEvents koleksiyonunu kontrol et
            eventDocRef.collection("dailyEvents")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        // Her belgeyi al ve EventModel'e çevirip listeye ekle
                        val event = document.getString("eventName")
                        val eventTime = document.getString("eventTime")
                        if (event != null && eventTime != null) {
                            val eventModel = EventModel(event, eventTime)
                            eventListArrayList.add(eventModel)
                        }
                    }

                    // RecyclerView'ı güncelle
                    eventAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda
                    // Hata durumu hakkında bir şey yapmanız gerekiyorsa buraya ekleyebilirsiniz.
                    Log.e("Firestore Error", exception.message.toString())
                }
        }
    }

}