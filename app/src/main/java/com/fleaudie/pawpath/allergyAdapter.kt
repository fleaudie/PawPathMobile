package com.fleaudie.pawpath

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class allergyAdapter (private val petAllergyList: List<userPet>): RecyclerView.Adapter<allergyAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val allergyName: TextView = itemView.findViewById(R.id.txtItemAllergyName)
        val allergyInfo : TextView = itemView.findViewById(R.id.txtItemAllergyInfo)
        val btnAddHealthInfo : ImageView = itemView.findViewById(R.id.imgbtnAddHealthInfo)

        init {
            btnAddHealthInfo.setOnClickListener {
                val context = itemView.context
                val itemAllergyName = allergyName.text.toString()
                val itemAllergyInfo = allergyInfo.text.toString()
                val intent = Intent(context, PetHealth::class.java)
                intent.putExtra("petName", itemAllergyName)
                intent.putExtra("petAge", itemAllergyInfo)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pet_allergy, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : userPet = petAllergyList[position]

        holder.allergyName.text = userpet.petName
        holder.allergyInfo.text = userpet.petUid
    }

    override fun getItemCount(): Int {
        return petAllergyList.size
    }
}