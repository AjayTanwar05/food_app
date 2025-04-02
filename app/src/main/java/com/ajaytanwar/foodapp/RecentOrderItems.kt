package com.ajaytanwar.foodapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.RecentBuyAdapter
import com.ajaytanwar.foodapp.databinding.ActivityRecentOrderItemsBinding
import com.ajaytanwar.foodapp.moodle.OrderDetails

class RecentOrderItems : AppCompatActivity() {

    private  lateinit var allFoodName : ArrayList<String>
    private lateinit var allFoodImage : ArrayList<String>
    private lateinit var allFoodPrice : ArrayList<String>
    private lateinit var allFoodQuantities : ArrayList<Int>

    private  val  binding : ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            finish()
        }

        val  recentOrderItems = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems ?.let { orderDetail ->
            if (orderDetail.isNotEmpty()){
                val recentOrderItem = orderDetail[0]
                allFoodName = recentOrderItem.foodName as ArrayList<String>
                allFoodPrice = recentOrderItem.foodprice as ArrayList<String>
                allFoodImage = recentOrderItem.foodImages as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantity as ArrayList<Int>
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv  = binding.reyclerViewRecentBuy
        rv.layoutManager =LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allFoodName,allFoodImage,allFoodPrice,allFoodQuantities)
        rv.adapter = adapter

    }
}