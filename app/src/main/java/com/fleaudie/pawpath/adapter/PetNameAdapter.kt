package com.fleaudie.pawpath.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.ui.PetProfile
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.UserPet

class PetNameAdapter(private val petNameList: List<UserPet>) : RecyclerView.Adapter<PetNameAdapter.MyViewHolder>() {
    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val petName : TextView = itemView.findViewById(R.id.txtItemPetName)
        val petAge : TextView = itemView.findViewById(R.id.txtItemPetAge)
        val petGender : TextView = itemView.findViewById(R.id.txtItemPetGender)
        val petWeight : TextView = itemView.findViewById(R.id.txtItemPetWeight)
        val petBreed : TextView = itemView.findViewById(R.id.txtItemPetBreed)
        val petUid : TextView = itemView.findViewById(R.id.txtItemPetUid)
        val consPet : ConstraintLayout = itemView.findViewById(R.id.consMyPet)
        init {
            consPet.setOnClickListener {
                val context = itemView.context
                val itemPetName = petName.text.toString()
                val itemPetAge = petAge.text.toString()
                val itemPetGender = petGender.text.toString()
                val itemPetWeight = petWeight.text.toString()
                val itemPetBreed = petBreed.text.toString()
                val itemPetUid = petUid.text.toString()
                val intent = Intent(context, PetProfile::class.java)
                intent.putExtra("petName", itemPetName)
                intent.putExtra("petAge", itemPetAge)
                intent.putExtra("petGender", itemPetGender)
                intent.putExtra("petWeight", itemPetWeight)
                intent.putExtra("petBreed", itemPetBreed)
                intent.putExtra("petUid", itemPetUid)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_pet, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return petNameList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : UserPet = petNameList[position]

        holder.petName.text = userpet.petName
        holder.petAge.text = userpet.petAge
        holder.petGender.text = userpet.petGender
        holder.petWeight.text = userpet.petWeight
        holder.petBreed.text = userpet.petBreed
        holder.petUid.text = userpet.petUid
    }
}
