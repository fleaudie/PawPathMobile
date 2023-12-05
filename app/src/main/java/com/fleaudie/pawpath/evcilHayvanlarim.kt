package com.fleaudie.pawpath

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class evcilHayvanlarim : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnAddPet : ImageButton
    private lateinit var txtPetName : EditText
    private lateinit var spnAnimalRace : Spinner
    private lateinit var spnAnimalBreed : Spinner
    private lateinit var txtPetAge : EditText
    private lateinit var txtPetWeight : EditText
    private lateinit var spnPetGender : Spinner
    private lateinit var rcyUserPets : RecyclerView
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

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        btnAddPet = findViewById(R.id.imgbtnAddPet)
        txtPetName = findViewById(R.id.txtPetName)
        spnAnimalRace = findViewById(R.id.spnType)
        spnAnimalBreed = findViewById(R.id.spnBreed)
        txtPetAge = findViewById(R.id.txtPetAge)
        txtPetWeight = findViewById(R.id.txtPetWeight)
        spnPetGender = findViewById(R.id.spnPetGender)
        rcyUserPets = findViewById(R.id.rcyUserPets)
        userPetArrayList = arrayListOf()
        myAdapter = petNameAdapter(userPetArrayList)
        rcyUserPets.layoutManager = LinearLayoutManager(this)
        rcyUserPets.setHasFixedSize(true)


        rcyUserPets.adapter = myAdapter

        EventChangeListener()

        val itemAnimalRace = arrayOf("Türü","Kedi", "Köpek", "Kuş")
        val adapterRace = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemAnimalRace)
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalRace.adapter = adapterRace

        val itemPetGender = arrayOf("Cinsiyeti","Dişi", "Erkek")
        val adapterGender = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemPetGender)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnPetGender.adapter = adapterGender

        //val itemAnimalBreed = arrayOf("Cinsi")
        //val itemAnimalBreed2 = arrayOf("Cinsi")

        spnAnimalRace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Birinci Spinner'dan seçilen öğeyi alın.
                val selectedItem = itemAnimalRace[position]

                // İkinci Spinner'ın içeriğini güncelleyin.
                updateSecondSpinner(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemleri buraya yazabilirsiniz.
            }
        }

        btnAddPet.setOnClickListener{
            addDataToFirestore()
            txtPetName.text.clear()
            txtPetAge.text.clear()
            spnAnimalRace.setSelection(0)
            spnAnimalBreed.setSelection(0)
            txtPetWeight.text.clear()
            spnPetGender.setSelection(0)
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
            view.setTextColor(Color.parseColor("#8EBC38"))

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent) as TextView

            // Spinner açıldığında görünen metin rengini burada ayarlayabilirsiniz.
            view.setTextColor(Color.parseColor("#D4E8B0"))

            return view
        }
    }

    private fun addDataToFirestore(){

        val petName = txtPetName.text.toString()
        val animalRace = spnAnimalRace.selectedItem.toString()
        val animalBreed = spnAnimalBreed.selectedItem.toString()
        val petAge = txtPetAge.text.toString()
        val petWeight = txtPetWeight.text.toString()
        val petGender = spnPetGender.selectedItem.toString()

        if (validateUserData(petName, animalRace, animalBreed, petAge, petWeight, petGender)) {
            val db = FirebaseFirestore.getInstance()
            if (currentUser != null) {
                val userDocRef =
                    db.collection("users").document(currentUser.uid).collection("userPets")
                val newPetDocRef = userDocRef.document()
                val userPets = hashMapOf(
                    "petUid" to newPetDocRef.id,
                    "petName" to petName,
                    "petRace" to animalRace,
                    "petBreed" to animalBreed,
                    "petAge" to petAge,
                    "petWeight" to petWeight,
                    "petGender" to petGender
                )
                newPetDocRef
                    .set(userPets)
                    .addOnSuccessListener { documentReference ->
                        // Başarılı
                        // Eğer başarılı olursa, belge referansını kullanabilirsiniz
                        val petUid = newPetDocRef.id
                        // Diğer işlemleri yapabilirsiniz
                    }
                    .addOnFailureListener { e ->
                        // Hata
                        // Firestore'a evcil hayvan bilgilerini eklerken bir hata oluştu
                    }
            }
        } else {
            // Kullanıcı bilgileri eksik, hata mesajını göster
            Toast.makeText(this, "Lütfen tüm bilgileri eksizsiz girin!", Toast.LENGTH_SHORT).show()
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

    private fun validateUserData(petName: String, animalRace: String, animalBreed: String, petAge: String, petWeight: String, petGender: String): Boolean {
        return petName.isNotBlank() && animalRace.isNotBlank() && animalBreed.isNotBlank() &&
                petAge.isNotBlank() && petWeight.isNotBlank() && petGender.isNotBlank()
    }

    private fun updateSecondSpinner(selectedItem: String) {
        spnAnimalBreed = findViewById(R.id.spnBreed)

        // İkinci Spinner'a gösterilecek seçenekleri belirleyin.
        val itemAnimalBreed2 = when (selectedItem) {
            "Kedi" -> arrayOf("American", "Ankara Kedisi", "Birman", "British", "Chantilly-Tiffany", "Cyprus", "Himalayan", "Oriental", "Persian", "Ragdoll", "Scottish Fold", "Siyan", "Sphynx", "Tekir", "Van Kedisi")
            "Köpek" -> arrayOf("Alman Kurdu", "Beagle", "Bichon Frise", "Borber Collie", "Buldog", "Çin Aslanı", "Chihuahua", "Dakhund", "Dalmaçya", "Doberman", "Golden", "Kangal", "Kaniş", "Labrador", "Pinscher", "Pitbull", "Sibirya Kurdu")
            "Kuş" -> arrayOf("Amerika Papağanı", "Amazon Papağanı", "Ara(Macaw) Papağanı", "Bengal İspinozu", "Cennet Papağanı", "Gri Papağan", "Hint Bülbülü", "Kakadu Papağanı", "Kakariki","Kanarya", "Kırmızı Arkalı Papağan", "Konur Papağanı", "Lori", "Muhabbet Kuşu", "Rosella", "Sarı Alınlı Papağan", "Şemsiye Kakadu", "Senegal Papağanı", "Sultan Papağanı", "Turuncu Kanatlı Papağan")
            else -> arrayOf("Cinsi")
        }


        // İkinci Spinner için ArrayAdapter'ı güncelleyin.
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemAnimalBreed2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalBreed.adapter = adapter2
        val adapterBreed = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemAnimalBreed2)
        adapterBreed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnAnimalBreed.adapter = adapterBreed
    }
}