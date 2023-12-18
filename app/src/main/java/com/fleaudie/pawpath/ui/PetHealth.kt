package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.AllergyAdapter
import com.fleaudie.pawpath.adapter.DiseaseAdapter
import com.fleaudie.pawpath.adapter.VaccineAdapter
import com.fleaudie.pawpath.data.HealthList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import de.hdodenhof.circleimageview.CircleImageView

class PetHealth : AppCompatActivity() {

    private lateinit var txtPetName : TextView
    private lateinit var txtPetBreed : TextView
    private lateinit var btnAddHealth : ImageButton
    private lateinit var db : FirebaseFirestore
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var rcyPetHealthAllergy : RecyclerView
    private lateinit var rcyPetHealthVaccine : RecyclerView
    private lateinit var rcyPetHealthDisease : RecyclerView
    private lateinit var allergyAdapter: AllergyAdapter
    private lateinit var vaccineAdapter : VaccineAdapter
    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var allergyListArrayList : ArrayList<HealthList>
    private lateinit var diseaseListArrayList : ArrayList<HealthList>
    private lateinit var vaccineListArrayList : ArrayList<HealthList>
    private lateinit var petUid: String
    private lateinit var imgPetHealthProfile : CircleImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_health)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
        imgPetHealthProfile = findViewById(R.id.imgPetHealthProfile)

        petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()
        if (currentUser != null) {
            val userPetDocRef = db.collection("users").document(currentUser.uid).collection("userPets").document(petUid)

            userPetDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Firestore belgesi varsa, profilFotoURL alanını al
                        val petPhotoUrl = documentSnapshot.getString("petPhotoUrl")

                        // Şimdi Glide ile ImageView'a resmi yükle
                        if (petPhotoUrl != null) {
                            Glide.with(this)
                                .load(petPhotoUrl)
                                .into(imgPetHealthProfile)
                        }
                        txtPetName.text = documentSnapshot.getString("petName")
                        txtPetBreed.text = documentSnapshot.getString("petBreed")
                    } else {
                        // Firestore belgesi yoksa, kullanıcıya bilgi ver
                        Toast.makeText(this, "Profil fotoğrafı bulunamadı.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Firestore'dan belge çekme sırasında bir hata oluştu
                    Toast.makeText(this, "Hata: $exception", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            txtPetName.text = intent.getStringExtra("petName")
            txtPetBreed.text = intent.getStringExtra("petBreed")
        }

        btnAddHealth = findViewById(R.id.imgbtnAddHealth)
        txtPetName = findViewById(R.id.txtPetHealthName)
        txtPetBreed = findViewById(R.id.txtPetHealthBreed)

        rcyPetHealthAllergy = findViewById(R.id.rcyPetHealthAllergy)
        allergyListArrayList = arrayListOf()
        allergyAdapter = AllergyAdapter(allergyListArrayList)
        rcyPetHealthAllergy.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyPetHealthAllergy.setHasFixedSize(true)
        rcyPetHealthAllergy.adapter = allergyAdapter

        rcyPetHealthVaccine = findViewById(R.id.rcyPetHealthVaccine)
        vaccineListArrayList = arrayListOf()
        vaccineAdapter = VaccineAdapter(vaccineListArrayList)
        rcyPetHealthVaccine.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyPetHealthVaccine.setHasFixedSize(true)

        rcyPetHealthVaccine.adapter = vaccineAdapter

        rcyPetHealthDisease = findViewById(R.id.rcyPetHealthDisease)
        diseaseListArrayList = arrayListOf()
        diseaseAdapter = DiseaseAdapter(diseaseListArrayList)
        rcyPetHealthDisease.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcyPetHealthDisease.setHasFixedSize(true)

        rcyPetHealthDisease.adapter = diseaseAdapter

        EventChangeListener()



        btnAddHealth.setOnClickListener {
            val intent = Intent(this, PetHealthAddInfo::class.java)
            intent.putExtra("petUid",petUid)
            startActivity(intent)
        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        petUid = intent.getStringExtra("petUid") ?: ""
        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets").document(petUid).collection("allergies")
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
                                allergyListArrayList.add(dc.document.toObject(HealthList::class.java))
                            }
                        }
                        allergyAdapter.notifyDataSetChanged()
                    }

                })
        }

        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets").document(petUid).collection("vaccines")
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
                                vaccineListArrayList.add(dc.document.toObject(HealthList::class.java))
                            }
                        }
                        vaccineAdapter.notifyDataSetChanged()
                    }

                })
        }

        if (userUID != null){
            db.collection("users").document(userUID).collection("userPets").document(petUid).collection("diseases")
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
                                diseaseListArrayList.add(dc.document.toObject(HealthList::class.java))
                            }
                        }
                        diseaseAdapter.notifyDataSetChanged()
                    }

                })
        }
    }
}