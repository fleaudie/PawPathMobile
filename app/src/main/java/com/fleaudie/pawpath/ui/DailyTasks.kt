package com.fleaudie.pawpath.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.DailyTasksAdapter
import com.fleaudie.pawpath.data.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class DailyTasks : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var rcyDailyTasks : RecyclerView
    private lateinit var myAdapter: DailyTasksAdapter
    private lateinit var taskArrayList : ArrayList<Task>
    private lateinit var btnEditDailyTasks : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_tasks)

        btnEditDailyTasks = findViewById(R.id.btnEditDailyTasks)

        rcyDailyTasks = findViewById(R.id.rcyDailyTasks)
        taskArrayList = arrayListOf()
        myAdapter = DailyTasksAdapter(taskArrayList)
        rcyDailyTasks.layoutManager = LinearLayoutManager(this)
        rcyDailyTasks.setHasFixedSize(true)


        rcyDailyTasks.adapter = myAdapter

        EventChangeListener()

        btnEditDailyTasks.setOnClickListener {
            val intent = Intent(this, AddDailyTasks::class.java)
            startActivity(intent)
        }
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid
        if (userUID != null){
            db.collection("users").document(userUID).collection("dailyTasks")
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
                                taskArrayList.add(dc.document.toObject(Task ::class.java))
                            }
                        }
                        myAdapter.notifyDataSetChanged()
                    }

                })
        }
    }

}