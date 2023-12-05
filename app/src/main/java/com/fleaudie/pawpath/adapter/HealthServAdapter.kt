package com.fleaudie.pawpath.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.ui.PetHealth
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.UserPet
import de.hdodenhof.circleimageview.CircleImageView

class HealthServAdapter(private val petNameList: List<UserPet>): RecyclerView.Adapter<HealthServAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.txtItemHealthServPetName)
        val imgPetProfile : CircleImageView = itemView.findViewById(R.id.imgItemHealthServPetPhoto)
        val petUid: TextView = itemView.findViewById(R.id.txtItemHealthServPetUid)
        val petBreed : TextView = itemView.findViewById(R.id.txtItemHealthServPetBreed)
        val btnPetHealth : ImageView = itemView.findViewById(R.id.imgItemClickPetHealth)
        val petAge : TextView = itemView.findViewById(R.id.txtItemHealthServPetAge)
        val petGender : TextView = itemView.findViewById(R.id.txtItemHealthServPetGender)
        val petWeight : TextView = itemView.findViewById(R.id.txtItemHealthServPetWeight)

        init {
            btnPetHealth.setOnClickListener {
                val context = itemView.context
                val itemPetName = petName.text.toString()
                val itemPetBreed = petBreed.text.toString()
                val itemPetUid = petUid.text.toString()
                val itemPetAge = petAge.text.toString()
                val itemPetGender = petGender.text.toString()
                val itemPetWeight = petWeight.text.toString()
                val intent = Intent(context, PetHealth::class.java)
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_health_serv, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : UserPet = petNameList[position]

        holder.petName.text = userpet.petName
        holder.petUid.text = userpet.petUid
        holder.petAge.text = userpet.petAge
        holder.petGender.text = userpet.petGender
        holder.petWeight.text = userpet.petWeight
        holder.petBreed.text = userpet.petBreed
    }

    override fun getItemCount(): Int {
        return petNameList.size
    }

}