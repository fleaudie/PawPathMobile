package com.fleaudie.pawpath

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlin.reflect.typeOf

class evcilHayvanlarim : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnEvcilHayvanEkle : ImageButton
    private lateinit var txtPetName : EditText
    private lateinit var spnAnimalRace : Spinner
    private lateinit var spnAnimalBreed : Spinner
    private lateinit var txtPetAge : EditText
    private lateinit var txtPetWeight : EditText
    private lateinit var txtPetColor : EditText
    private lateinit var rcyEvcilHayvanEkle : RecyclerView
    private lateinit var myAdapter: petNameAdapter
    private lateinit var userPetArrayList : ArrayList<userPet>
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.evcil_hayvanlarim)

        val bottomNav = findViewById<BottomNavigationView>(R.id.navPets)
        menu.setupBottomNavigation(this, bottomNav)
        bottomNav.menu.findItem(R.id.navMain).setIcon(R.drawable.home_icon)
        bottomNav.menu.findItem(R.id.navTakvim).setIcon(R.drawable.calendar_icon)
        bottomNav.menu.findItem(R.id.navPets).setIcon(R.drawable.paw_icon_click)
        bottomNav.menu.findItem(R.id.navProfile).setIcon(R.drawable.profile_icon)

        btnEvcilHayvanEkle = findViewById<ImageButton>(R.id.imgbtnEvcilHayvanEkle)
        txtPetName = findViewById<EditText>(R.id.txtHayvanAdi)
        spnAnimalRace = findViewById<Spinner>(R.id.spnIrk)
        spnAnimalBreed = findViewById<Spinner>(R.id.spnCins)
        txtPetAge = findViewById<EditText>(R.id.txtHayvanYasi)
        txtPetWeight = findViewById<EditText>(R.id.txtHayvanKilo)
        txtPetColor = findViewById<EditText>(R.id.txtHayvanRengi)
        rcyEvcilHayvanEkle = findViewById(R.id.rcyEvcilHayvanEkle)
        userPetArrayList = arrayListOf()
        myAdapter = petNameAdapter(userPetArrayList)
        rcyEvcilHayvanEkle.layoutManager = LinearLayoutManager(this)
        rcyEvcilHayvanEkle.setHasFixedSize(true)


        rcyEvcilHayvanEkle.adapter = myAdapter

        EventChangeListener()

        val itemAnimalRace = arrayOf("Kedi", "Köpek", "Kuş")
        val adapterRace = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemAnimalRace)
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalRace.adapter = adapterRace

        val itemAnimalBreed = arrayOf("Kedi", "Köpek", "Kuş")
        val adapterBreed = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemAnimalBreed)
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalBreed.adapter = adapterBreed

        btnEvcilHayvanEkle.setOnClickListener{
            addDataToFirestore()
        }



    }
    private class CustomArrayAdapter(
        context: Context,
        resource: Int,
        objects: Array<String>
    ) : ArrayAdapter<String>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView

            // Spinner içindeki metin rengini burada ayarlayabilirsiniz.
            view.setTextColor(Color.BLUE)

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent) as TextView

            // Spinner açıldığında görünen metin rengini burada ayarlayabilirsiniz.
            view.setTextColor(Color.RED)

            return view
        }
    }

    private fun addDataToFirestore(){

        val petName = txtPetName.text.toString()
        val animalRace = spnAnimalRace.selectedItem.toString()
        val animalBreed = spnAnimalBreed.selectedItem.toString()
        val petAge = txtPetAge.text.toString()
        val petWeight = txtPetWeight.text.toString()
        val petColor = txtPetColor.text.toString()
        val db = FirebaseFirestore.getInstance()
        if (currentUser != null){
            val userPets = hashMapOf(
                "petName" to petName,
                "petRace" to animalRace,
                "petBreed" to animalBreed,
                "petAge" to petAge,
                "petWeight" to petWeight,
                "petColor" to petColor
            )
            db.collection("users")
                .document(currentUser.uid)
                .collection("userPets")
                .add(userPets)
                .addOnSuccessListener { documentReference -> }
                .addOnFailureListener { e -> }

        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets")
                .addSnapshotListener(object : EventListener<QuerySnapshot>{
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
                                userPetArrayList.add(dc.document.toObject(userPet ::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }
    }
}