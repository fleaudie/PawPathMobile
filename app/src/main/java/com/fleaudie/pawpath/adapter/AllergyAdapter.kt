package com.fleaudie.pawpath.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.HealthList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllergyAdapter (private val petHealthList: List<HealthList>): RecyclerView.Adapter<AllergyAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val allergyName : TextView = itemView.findViewById(R.id.txtItemAllergyName)
        val allergyInfo : TextView = itemView.findViewById(R.id.txtItemAllergyInfo)
        val btnItemDeleteAllergy : Button = itemView.findViewById(R.id.btnItemDeleteAllergy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_allergy, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val healthList : HealthList = petHealthList[position]

        holder.allergyName.text = healthList.allergyName
        holder.allergyInfo.text = healthList.allergyInfo

        holder.btnItemDeleteAllergy.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return petHealthList.size
    }
}