package com.fleaudie.pawpath

import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class petAdapterMain(private val petNameList: List<userPet>): RecyclerView.Adapter<petAdapterMain.MyViewHolder>() {

    private var isAddPetVisible = true  // Bu değişkeni ekledik ve başlangıçta true yaptık.

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.txtMainItemPetName)
        val imgPetProfile : CircleImageView = itemView.findViewById(R.id.imgPetImageMain)
        val imgAddPet : ImageView = itemView.findViewById(R.id.imgAddPetMain)
        val txtAddPet : TextView = itemView.findViewById(R.id.txtAddPetMain)
        val imgAddPetPlus : ImageView = itemView.findViewById(R.id.imgAddPetPlusMain)
        val petUid: TextView = itemView.findViewById(R.id.txtMainItemPetUid)
        val petAge : TextView = itemView.findViewById(R.id.txtMainItemPetAge)
        val petGender : TextView = itemView.findViewById(R.id.txtMainItemPetGender)
        val petWeight : TextView = itemView.findViewById(R.id.txtMainItemPetWeight)
        val petBreed : TextView = itemView.findViewById(R.id.txtMainItemPetBreed)

        init {
            imgPetProfile.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Dokunma başladığında yukarı doğru animasyonu başlat
                        val slideUp = TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, -0.1f
                        )
                        slideUp.duration = 200
                        slideUp.fillAfter = true
                        view.startAnimation(slideUp)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Dokunma sona erdiğinde veya iptal edildiğinde aşağı doğru animasyonu başlat
                        val slideDown = TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, -0.1f,
                            Animation.RELATIVE_TO_SELF, 0f
                        )
                        slideDown.duration = 200
                        slideDown.fillAfter = true
                        view.startAnimation(slideDown)
                    }
                }
                false
            }
            imgPetProfile.setOnClickListener {
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_evcilhayvan_main, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userpet : userPet = petNameList[position]

        if (isAddPetVisible) {
            holder.imgAddPet.visibility = View.VISIBLE
            isAddPetVisible = false  // Bir kez göründü, bir daha görünmesin
            holder.txtAddPet.visibility = View.VISIBLE
            isAddPetVisible = false
            holder.imgAddPetPlus.visibility = View.VISIBLE
            isAddPetVisible = false
        } else {
            holder.imgAddPet.visibility = View.GONE
            holder.txtAddPet.visibility = View.GONE
            holder.imgAddPetPlus.visibility = View.GONE
        }


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