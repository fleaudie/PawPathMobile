package com.fleaudie.pawpath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class HealthService : AppCompatActivity() {

    private lateinit var btnHealthServAddPet : ImageButton
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var rcyHealthServ : RecyclerView
    private lateinit var myAdapter: HealthServAdapter
    private lateinit var userPetArrayList : ArrayList<userPet>
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.health_service)

        rcyHealthServ = findViewById(R.id.rcyHealthServ)
        userPetArrayList = arrayListOf()
        myAdapter = HealthServAdapter(userPetArrayList)
        rcyHealthServ.layoutManager = LinearLayoutManager(this)
        rcyHealthServ.setHasFixedSize(true)
        rcyHealthServ.adapter = myAdapter
        EventChangeListener()

        btnHealthServAddPet = findViewById(R.id.imgbtnHealthServAddPet)
        btnHealthServAddPet.setOnClickListener{
            val intent = Intent(this, evcilHayvanlarim::class.java)
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
                                userPetArrayList.add(dc.document.toObject(userPet ::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }
    }
}