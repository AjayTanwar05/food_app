package com.ajaytanwar.foodapp.fregment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.BuyAgainAdapter
import com.ajaytanwar.foodapp.RecentOrderItems
import com.ajaytanwar.foodapp.databinding.FragmentHistoryBinding
import com.ajaytanwar.foodapp.moodle.OrderDetails
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class historyFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        //initialize for the firebase auth
        auth = FirebaseAuth.getInstance()

        //initialize for the firebase database
        database = FirebaseDatabase.getInstance()

        //Retrieve and display the user  order History
        retriveBuyHitsory()

        // recent buy button click
        binding.recentbuyitem.setOnClickListener {
            seeItemRecentBuy()
        }
        binding.receivedButton.setOnClickListener{

            updateOrderStatus()
        }





        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completedReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completedReference.child("paymentReceived").setValue(true)

    }

    // function to see items recent buy
    private fun seeItemRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem", listOfOrderItem)
            startActivity(intent)
        }

    }


    // function to see items recent buy to history
    private fun retriveBuyHitsory() {
        binding.recentbuyitem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistroyItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistroyItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setDataInRecentBuyItem() {
        binding.recentbuyitem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                buyAgainFoodName.text = it.foodName?.firstOrNull()?:""
                buyAgainFoodPrice.text = it.foodprice?.firstOrNull()?:""

                val image = it.foodImages?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainImage)

                val isOrderIsAccpted = listOfOrderItem[0].orderAccepted
                if (isOrderIsAccpted){
                    ordercount.background.setTint(Color.GREEN)
                    receivedButton.visibility = View.VISIBLE
                }


            }
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodName?.firstOrNull()?.let { buyAgainFoodName.add(it)
                listOfOrderItem[i].foodprice?.firstOrNull()?.let { buyAgainFoodPrice.add(it)
                    listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it)
                    }
                }
                val rv = binding.BuyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = BuyAgainAdapter(
                    buyAgainFoodName,
                    buyAgainFoodPrice,
                    buyAgainFoodImage,
                    requireContext()
                )
                rv.adapter = buyAgainAdapter
            }

        }
    }
}




