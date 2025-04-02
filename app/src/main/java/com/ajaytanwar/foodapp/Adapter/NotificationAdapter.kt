package com.ajaytanwar.foodapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.foodapp.databinding.NotificationItemsBinding

class NotificationAdapter(private val Notification : ArrayList<String>,private val notificationimage : ArrayList<Int>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int =  Notification.size

    inner class NotificationViewHolder(private var binding: NotificationItemsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                NotificationTextView.text  = Notification[position]
                notificationImageView.setImageResource(notificationimage[position])
            }
        }

    }
}