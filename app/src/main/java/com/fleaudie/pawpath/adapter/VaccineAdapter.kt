package com.fleaudie.pawpath.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.HealthList

class VaccineAdapter (private val petHealthList: List<HealthList>): RecyclerView.Adapter<VaccineAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vaccineName : TextView = itemView.findViewById(R.id.txtItemVaccineName)
        val vaccineDate : TextView = itemView.findViewById(R.id.txtItemVaccineDate)
        val btnItemDeleteVaccine : Button = itemView.findViewById(R.id.btnItemDeleteVaccine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_vaccine, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val healthList : HealthList = petHealthList[position]

        holder.vaccineName.text = healthList.vaccineName
        holder.vaccineDate.text = healthList.vaccineDate

        holder.btnItemDeleteVaccine.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return petHealthList.size
    }
}