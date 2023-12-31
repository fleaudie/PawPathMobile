package com.fleaudie.pawpath.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.EventModel

class EventAdapter(private val events: MutableList<EventModel>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEventName: TextView = itemView.findViewById(R.id.txtEvent)
        val textEventTime: TextView = itemView.findViewById(R.id.txtEventTime)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event : EventModel = events[position]
        holder.textEventName.text = event.eventName
        holder.textEventTime.text = event.eventTime

        holder.btnDelete.setOnClickListener {
        }
    }

    override fun getItemCount(): Int = events.size
}