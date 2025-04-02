package com.ajaytanwar.foodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ajaytanwar.foodapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomnav.setupWithNavController(NavController)
        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = Notification_buttom()
            bottomSheetDialog.show(supportFragmentManager,"Test")

        }

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,loginactivity::class.java)
            startActivity(intent)
        }

        refreshapp()

    }



    private fun refreshapp() {
        binding.refreshToswip.setOnClickListener {

            Toast.makeText(this,"page refresh" ,Toast.LENGTH_SHORT).show()

            binding.refreshToswip.isRefreshing = true
        }
    }


}