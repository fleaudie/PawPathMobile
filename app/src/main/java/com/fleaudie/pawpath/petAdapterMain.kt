package com.fleaudie.pawpath

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class petAdapterMain(private val petNameList: List<userPet>): RecyclerView.Adapter<petAdapterMain.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.txtItemPetName)
        val petUid: TextView = itemView.findViewById(R.id.txtItemPetUid)
        val consPet: ConstraintLayout = itemView.findViewById(R.id.consItemEvcilMain)

        init {
            consPet.setOnClickListener {
                val context = itemView.context
                val itemPetName = petName.text.toString()
                val itemPetUid = petUid.text.toString()
                val intent = Intent(context, PetProfile::class.java)
                intent.putExtra("petName", itemPetName)
                intent.putExtra("petUid", itemPetUid)
                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_evcilhayvan_main, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : userPet = petNameList[position]
        holder.petName.text = userpet.petName
        holder.petUid.text = userpet.petUid
    }

    override fun getItemCount(): Int {
        return petNameList.size
    }

}