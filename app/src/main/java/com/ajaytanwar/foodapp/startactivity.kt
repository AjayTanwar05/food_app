package com.ajaytanwar.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ajaytanwar.foodapp.databinding.ActivityStartactivityBinding

class startactivity : AppCompatActivity() {
    private val binding: ActivityStartactivityBinding by lazy {
        ActivityStartactivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.nextbutton.setOnClickListener {
            val intent = Intent(this,loginactivity::class.java)
            startActivity(intent)
        }
    }




    }






