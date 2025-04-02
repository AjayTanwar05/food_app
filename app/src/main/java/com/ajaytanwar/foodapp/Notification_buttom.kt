package com.ajaytanwar.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.NotificationAdapter
import com.ajaytanwar.foodapp.databinding.FragmentNotificationButtomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_buttom : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentNotificationButtomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationButtomBinding.inflate(layoutInflater,container,false)
        val notification = listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationImages = listOf(R.drawable.sademoji,
                                         R.drawable.bus,
                                        R.drawable.illustration
        )
        val adapter = NotificationAdapter(
            ArrayList(notification),
            ArrayList(notificationImages)
        )
        binding.notificationRecyclerView.layoutManager  = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}