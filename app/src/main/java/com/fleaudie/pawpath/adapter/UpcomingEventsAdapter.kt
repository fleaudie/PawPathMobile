package com.fleaudie.pawpath.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fleaudie.pawpath.R
import com.fleaudie.pawpath.data.EventModel

class UpcomingEventsAdapter(private val events: List<EventModel>) :
    RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEventName: TextView = itemView.findViewById(R.id.txtUpcomingEventName)
        val textEventTime: TextView = itemView.findViewById(R.id.txtUpcomingEventTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_plan_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event: EventModel = events[position]
        holder.textEventName.text = event.eventName
        holder.textEventTime.text = event.eventTime
    }

    override fun getItemCount(): Int = events.size
}