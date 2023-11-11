package com.fleaudie.pawpath

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class petNameAdapter(private val petNameList: List<userPet>) : RecyclerView.Adapter<petNameAdapter.MyViewHolder>() {
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val petName : TextView = itemView.findViewById(R.id.txtEvcilHayvanAdi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_evcilhayvan, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return petNameList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : userPet = petNameList[position]
        holder.petName.text = userpet.petName
    }
}
