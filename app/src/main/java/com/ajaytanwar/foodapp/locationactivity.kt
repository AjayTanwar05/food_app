package com.ajaytanwar.foodapp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.foodapp.databinding.ActivityLocationactivityBinding

class locationactivity : AppCompatActivity() {
    private val binding: ActivityLocationactivityBinding by lazy {
        ActivityLocationactivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val locationlist = arrayOf("jaipur" , "patna" , "mumbai" , "pali")
        val adapter = ArrayAdapter(this,android.R.layout.activity_list_item,locationlist)
        val appCompatAutoCompleteTextView = binding.litsoflocation
        appCompatAutoCompleteTextView.setAdapter(adapter)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}