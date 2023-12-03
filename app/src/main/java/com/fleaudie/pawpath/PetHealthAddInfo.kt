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
import android.widget.Toast
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

        btnAddHealthInfo = findViewById(R.id.imgbtnAddHealthInfo)
        txtAllergyInfo = findViewById(R.id.txtAllergyInfo)
        txtAllergyName = findViewById(R.id.txtAllergyName)
        spnVaccineName = findViewById(R.id.spnVaccineName)
        spnVaccineDate = findViewById(R.id.spnVaccineDate)
        spnDiseaseName = findViewById(R.id.spnDiseaseName)

        // Aşı Tarihi
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnVaccineDate.adapter = adapter
        // Spinner'a tıklanıldığında tarih seçici diyalogunu göster
        spnVaccineDate.setOnTouchListener { _, _ ->
            showDatePickerDialog()
            true
        }

        petUid = intent.getStringExtra("petUid") ?: ""

        btnAddHealthInfo.setOnClickListener {
            updateHealthInfo()
        }
    }

    private fun updateHealthInfo() {
        val allergyName = txtAllergyName.text.toString()
        val allergyInfo = txtAllergyInfo.text.toString()
        petUid = intent.getStringExtra("petUid") ?: ""
        db = FirebaseFirestore.getInstance()

        if (currentUser != null){
            val allergy = hashMapOf(
                "allergyName" to allergyName,
                "allergyInfo" to allergyInfo
            )
            db.collection("users").document(currentUser.uid).collection("userPets").document(petUid).collection("allergys")
                .add(allergy)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

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
}