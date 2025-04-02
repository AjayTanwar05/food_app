package com.ajaytanwar.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.foodapp.Details_activity
import com.ajaytanwar.foodapp.databinding.MenuItemBinding
import com.ajaytanwar.foodapp.moodle.MenuItem
import com.bumptech.glide.Glide

class menuAdapter(
    private val menuItems: List<MenuItem>,
    private val  requireContext: Context) : RecyclerView.Adapter<menuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
       val binding=  MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int =menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsactivity(position)
                }
                // set

            }
        }

        private fun openDetailsactivity(position: Int) {
            val menuItem = menuItems[position]


            val intent = Intent(requireContext,Details_activity::class.java).apply {
                putExtra("MenuItemName",menuItem.foodname)
                putExtra("MenuItemImage",menuItem.foodImage)
                putExtra("MenuItemDescription",menuItem.foodDescription)
                putExtra("MenuItemIngredient",menuItem.foodIngredirnt)
                putExtra("MenuItemPrice",menuItem.foodprice)
            }
            requireContext.startActivity(intent)

        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menufoodName.text= menuItem.foodname
                menuPrice.text= menuItem.foodprice
               val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)

            }

        }
    }

}







