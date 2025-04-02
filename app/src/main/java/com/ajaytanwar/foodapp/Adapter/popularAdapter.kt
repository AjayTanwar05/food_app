package com.ajaytanwar.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.foodapp.Details_activity
import com.ajaytanwar.foodapp.databinding.PopularItemBinding

class popularAdapter(private val items:List<String> ,private val price:List<String>,private val images:List<Int>,private val requireContext: Context) : RecyclerView.Adapter<popularAdapter.PouplerViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PouplerViewHolder {
        return PouplerViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }



    override fun onBindViewHolder(holder: PouplerViewHolder, position: Int) {
        val item = items[position]
        val images = images[position]
        val price = price[position]
        holder.bind(item,price,images)

        holder.itemView.setOnClickListener{
            val intent = Intent(requireContext, Details_activity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("MenuItemImage",images)
            requireContext.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
return items.size
    }
    class PouplerViewHolder(private val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root){
       private val imagesView = binding.imageView7
        fun bind(item: String, price: String, images: Int) {
            binding.foodnamepopuler.text  = item
            binding.pricepopuler.text = price
            imagesView.setImageResource(images)

        }

    }

}

