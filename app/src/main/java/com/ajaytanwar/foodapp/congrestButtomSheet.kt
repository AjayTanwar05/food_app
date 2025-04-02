package com.ajaytanwar.foodapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ajaytanwar.foodapp.databinding.ActivityMainBinding
import com.ajaytanwar.foodapp.databinding.FragmentCongrestButtomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class congrestButtomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongrestButtomSheetBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongrestButtomSheetBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        binding.gohome.setOnClickListener{
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

    }
}