package com.fleaudie.pawpath

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PetHealthAddInfo : AppCompatActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var btnAddHealthInfo : ImageButton
    private lateinit var txtAllergyName : EditText
    private lateinit var txtAllergyInfo : EditText
    private lateinit var spnVaccineName : Spinner
    private lateinit var spnVaccineDate : Spinner
    private lateinit var spnDiseaseName : Spinner
    private lateinit var rcyUserPets : RecyclerView
    private lateinit var myAdapter: petNameAdapter
    private lateinit var userPetArrayList : ArrayList<userPet>
    private lateinit var db : FirebaseFirestore
    private lateinit var currentUserUid: String
    private lateinit var petUid: String
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_health_add_info)

        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)

        btnAddHealthInfo = findViewById(R.id.imgbtnAddHealthInfo)
        txtAllergyInfo = findViewById(R.id.txtAllergyInfo)
        txtAllergyName = findViewById(R.id.txtAllergyName)
        spnVaccineName = findViewById(R.id.spnVaccineName)
        spnVaccineDate = findViewById(R.id.spnVaccineDate)
        spnDiseaseName = findViewById(R.id.spnDiseaseName)

        // Aşı Adı
        val itemVaccineName = arrayOf("")
        val adapterVaccine = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemVaccineName)
        adapterVaccine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVaccineName.adapter = adapterVaccine

        spnVaccineName.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // İlgili spinner açıldığında burası çalışır
                updateVacSpinnerContent(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // İlgili spinner'dan hiçbir şey seçilmediğinde burası çalışır
            }
        })


        // Aşı Tarihi
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVaccineDate.adapter = adapter
        // Spinner'a tıklanıldığında tarih seçici diyalogunu göster
        spnVaccineDate.setOnTouchListener { _, _ ->
            showDatePickerDialog()
            true
        }

        // Hastalık Adı
        val itemDiseaseName = arrayOf("")
        val adapterDisease = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, itemDiseaseName)
        adapterDisease.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnDiseaseName.adapter = adapterDisease

        spnDiseaseName.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // İlgili spinner açıldığında burası çalışır
                updateDisSpinnerContent(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // İlgili spinner'dan hiçbir şey seçilmediğinde burası çalışır
            }
        })

        petUid = intent.getStringExtra("petUid") ?: ""

        btnAddHealthInfo.setOnClickListener {
            updateHealthInfo()
            txtAllergyName.text.clear()
            txtAllergyInfo.text.clear()
        }
    }

    private fun updateHealthInfo() {
        val allergyName = txtAllergyName.text.toString()
        val allergyInfo = txtAllergyInfo.text.toString()
        petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()

        if (currentUser != null){
            if (allergyName.isNotEmpty() && allergyInfo.isNotEmpty()) {
                val allergy = hashMapOf(
                    "allergyName" to allergyName,
                    "allergyInfo" to allergyInfo
                )
                db.collection("users").document(currentUser.uid).collection("userPets")
                    .document(petUid).collection("allergys")
                    .add(allergy)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }

        val vaccineName = spnVaccineName.selectedItem.toString()
        val vaccineDate = spnVaccineDate.selectedItem?.toString() ?: ""
        if (currentUser != null){
            if (vaccineName != "Aşı Adı" && vaccineName.isNotEmpty() && vaccineDate != "Son Aşı Tarihi" && vaccineDate.isNotEmpty()) {
                val vaccine = hashMapOf(
                    "vaccineName" to vaccineName,
                    "vaccineDate" to vaccineDate
                )
                db.collection("users").document(currentUser.uid).collection("userPets")
                    .document(petUid).collection("vaccines")
                    .add(vaccine)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }

        val diseaseName = spnDiseaseName.selectedItem.toString()
        if (currentUser != null){
            if (diseaseName != "Hastalık Adı" && diseaseName.isNotEmpty()) {
                val disease = hashMapOf(
                    "diseaseName" to diseaseName
                )
                db.collection("users").document(currentUser.uid).collection("userPets")
                    .document(petUid).collection("diseases")
                    .add(disease)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener {

                    }
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateSpinner()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateSpinner() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = sdf.format(calendar.time)

        val adapter = spnVaccineDate.adapter as ArrayAdapter<String>
        adapter.clear()
        adapter.add(formattedDate)
        adapter.notifyDataSetChanged()
    }

    private class CustomArrayAdapter(
        context: Context,
        resource: Int,
        objects: Array<String>
    ) : ArrayAdapter<String>(context, resource, objects) {
        private var data: MutableList<String> = objects.toMutableList()
        override fun clear() {
            data = mutableListOf()
            notifyDataSetChanged()
        }

        override fun addAll(collection: Collection<String>) {
            data.addAll(collection)
            notifyDataSetChanged()
        }

        override fun addAll(vararg items: String) {
            data.addAll(items)
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView

            // Spinner içindeki metin rengini burada ayarlayabilirsiniz.
            view.setTextColor(Color.parseColor("#1E6091"))

            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent) as TextView

            // Spinner açıldığında görünen metin rengini burada ayarlayabilirsiniz.
            view.setTextColor(Color.parseColor("#1689AC"))

            return view
        }
    }

    private fun updateVacSpinnerContent(selectedPosition: Int) {
        petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()

        if (currentUser != null) {
            val petRaceQuery = db.collection("users").document(currentUser.uid).collection("userPets")
                .document(petUid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val petRace = document.getString("petRace")
                        if (petRace != null) {
                            // Evcil hayvanın türüne göre aşıları yükle
                            loadVaccinesForPetRace(petRace, selectedPosition)
                        }
                    }
                }
        }
    }

    private fun loadVaccinesForPetRace(petRace: String, selectedPosition: Int) {
        // Burada, eğer evcil hayvan kedi ise istediğiniz aşıları ekleyebilirsiniz
        val newData = if (petRace == "Kedi") {
            arrayOf("Aşı Adı", "İç Dış Parazit Aşısı", "Karma Aşı", "Kuduz Aşısı", "Lösemi Aşısı")
        } else if(petRace == "Köpek"){
            // Eğer evcil hayvan köpek ise istediğiniz köpek aşılarını ekleyebilirsiniz
            arrayOf("Aşı Adı", "Karma Aşı", "Bronşin KC Aşısı", "Bordotella Aşısı", "İç Dış Parazit Aşısı", "Kuduz Aşısı", "Lyme Aşısı")
        } else{
            arrayOf("Aşı Adı", "Paramyxovirus Aşısı", "Poxvirus Aşısı", "Klamidya Aşısı", "Batı Nil Virusu Aşısı", "Polyomavirus Aşısı")
        }

        updateVacSpinnerAdpContent(newData, selectedPosition)
    }

    private fun updateVacSpinnerAdpContent(newData: Array<String>, selectedPosition: Int) {
        val adapter = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, newData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVaccineName.adapter = adapter

        // Şeçili pozisyonu güncelle
        spnVaccineName.setSelection(selectedPosition)
    }

    private fun updateDisSpinnerContent(selectedPosition: Int) {
        petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()

        if (currentUser != null) {
            val petRaceQuery = db.collection("users").document(currentUser.uid).collection("userPets")
                .document(petUid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val petRace = document.getString("petRace")
                        if (petRace != null) {
                            // Evcil hayvanın türüne göre aşıları yükle
                            loadDiseasesForPetRace(petRace, selectedPosition)
                        }
                    }
                }
        }
    }

    private fun loadDiseasesForPetRace(petRace: String, selectedPosition: Int) {
        // Burada, eğer evcil hayvan kedi ise istediğiniz aşıları ekleyebilirsiniz
        val newData = if (petRace == "Kedi") {
            arrayOf("Hastalık Adı", "Bağırsak İltihabı", "Böbrek Yetmezliği", "Diyabet", "FIP", "FIV", "İdrar Yolu Enfeksiyonu", "Kalp Kurdu", "Kanser", "Kedi Gençlik Hastalığı", "Kedi Lösemisi", "Kuduz", "Mantar", "Solunum Yolu İltihabı")
            // Eğer evcil hayvan köpek ise istediğiniz köpek aşılarını ekleyebilirsiniz
        } else if(petRace == "Köpek"){
            arrayOf("Hastalık Adı", "Bağırsak Parazitleri", "Barınak Hastalığı", "Bulaşıcı Karaciğer Hastalığı", "Deri Enfeksiyonları", "Diş Eti Hastalıkları", "Diyabet", "Dış Parazit", "Gençlik Hastalığı", "İshal", "Kalp kurdu", "Kanlı İshal", "Kanser", "Kennel Öksürük", "Kuduz", "Kulak Enfeksiyonu", "Leishmaniasis", "Leptospira", "Parvovirüs", "Saçkıran", "Uyuz")
        } else{
            arrayOf("Hastalık Adı", "Astım", "Çiçek Hastalığı", "E-Coli", "Fransız Tüy Dökümü", "Guatr", "Haemophillus", "Kahverengi Hipertrofisi", "Kandida", "Kanser", "Karaciğer Hastalığı", "Klamidya Psittaci", "Kolera", "Kuruma Hastalığı", "Mantar", "Ornithosis", "Pamukçuk", "Papağan Ateşi", "Polyoma Virüs", "Psittacine", "Salmonella", "Streptococcosis", "Trichomonas", "Tuberculosis", "Uyuz")
        }

        updateDisSpinnerAdpContent(newData, selectedPosition)
    }

    private fun updateDisSpinnerAdpContent(newData: Array<String>, selectedPosition: Int) {
        val adapter = CustomArrayAdapter(this, android.R.layout.simple_spinner_item, newData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnDiseaseName.adapter = adapter

        // Şeçili pozisyonu güncelle
        spnDiseaseName.setSelection(selectedPosition)
    }

}