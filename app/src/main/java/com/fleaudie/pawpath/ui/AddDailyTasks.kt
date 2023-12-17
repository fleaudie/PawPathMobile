package com.fleaudie.pawpath.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.EmojiDialogFragment
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.adapter.AddDailyTasksAdapter
import com.fleaudie.pawpath.adapter.PetNameAdapter
import com.fleaudie.pawpath.data.Task
import com.fleaudie.pawpath.data.UserPet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class AddDailyTasks : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var txtAddTaskName: EditText
    private lateinit var db : FirebaseFirestore
    private var selectedEmojiResourceId: Int = 0
    private lateinit var btnAddTask : Button
    private lateinit var btnSelectEmoji : ImageView
    private lateinit var selectedEmoji: String  // Bu satırı ekleyin
    private lateinit var rcyAddDailyTasks : RecyclerView
    private lateinit var myAdapter: AddDailyTasksAdapter
    private lateinit var taskArrayList : ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_daily_tasks)

        txtAddTaskName = findViewById(R.id.txtAddTaskName)
        btnAddTask = findViewById(R.id.btnAddTask)
        btnSelectEmoji = findViewById(R.id.btnSelectEmoji)

        // Emoji seçimini başlat
        btnSelectEmoji.setOnClickListener{
            showEmojiDialog()
        }

        btnAddTask.setOnClickListener {
            addDataToFirestore()
            txtAddTaskName.text.clear()
        }

        rcyAddDailyTasks = findViewById(R.id.rcyAddDailyTasks)
        taskArrayList = arrayListOf()
        myAdapter = AddDailyTasksAdapter(taskArrayList)
        rcyAddDailyTasks.layoutManager = LinearLayoutManager(this)
        rcyAddDailyTasks.setHasFixedSize(true)


        rcyAddDailyTasks.adapter = myAdapter

        EventChangeListener()

    }

    private fun showEmojiDialog() {
        val emojiDialogFragment = EmojiDialogFragment { selectedEmoji ->
            val emojiResourceId = resources.getIdentifier(selectedEmoji, "drawable", packageName)
            val emojiDrawable = getDrawable(emojiResourceId)
            btnSelectEmoji.setImageDrawable(emojiDrawable)

            // selectedEmoji'yi sınıf düzeyinde bir değişken olarak tanımla
            this.selectedEmoji = selectedEmoji
            this.selectedEmojiResourceId = emojiResourceId
        }
        emojiDialogFragment.show(supportFragmentManager, "EmojiDialog")
    }

    private fun addDataToFirestore() {
        val taskName = txtAddTaskName.text.toString()

        if (validateTaskData(taskName, selectedEmojiResourceId)) {

            if (currentUser != null) {
                db = FirebaseFirestore.getInstance()

                // Seçilen emoji adının kaynak ismini al
                val emojiName = resources.getResourceEntryName(selectedEmojiResourceId)

                // Firestore'a gönderilecek Task objesini oluştur
                val newTask = Task(taskName, emojiName, selectedEmojiResourceId)

                // Firestore'a görevi ekle
                currentUser?.let {
                    db.collection("users")
                        .document(it.uid)
                        .collection("dailyTasks")
                        .add(newTask)
                        .addOnSuccessListener { documentReference ->
                            // Belge başarıyla eklendi
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            // Hata durumunda
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
        } else {
            // Kullanıcı bilgileri eksik, hata mesajını göster
            Toast.makeText(this, "Lütfen tüm bilgileri eksizsiz girin!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateTaskData(taskName: String, emojiResourceId: Int): Boolean {
        return taskName.isNotBlank() && emojiResourceId != 0
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