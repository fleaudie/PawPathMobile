package com.fleaudie.pawpath.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.Task

class AddDailyTasksAdapter (private val taskList: List<Task>): RecyclerView.Adapter<AddDailyTasksAdapter.MyViewHolder>() {

    public class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.txtItemTaskName)
        val imgItemTaskEmoji : ImageView = itemView.findViewById(R.id.imgItemTaskEmoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_add_daily_tasks, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.taskName
        holder.imgItemTaskEmoji.setImageResource(task.emojiResourceId)
        // Diğer view bileşenlerini de burada doldurabilirsiniz
    }
}