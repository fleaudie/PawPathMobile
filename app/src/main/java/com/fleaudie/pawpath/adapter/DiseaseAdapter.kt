package com.fleaudie.pawpath.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.HealthList

class DiseaseAdapter (private val petDiseaseList: List<HealthList>): RecyclerView.Adapter<DiseaseAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diseaseName : TextView = itemView.findViewById(R.id.txtItemDiseaseName)
        val btnItemDeleteDisease : Button = itemView.findViewById(R.id.btnItemDeleteDisease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_disease, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val healthList : HealthList = petDiseaseList[position]

        holder.diseaseName.text = healthList.diseaseName

        holder.btnItemDeleteDisease.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return petDiseaseList.size
    }
}