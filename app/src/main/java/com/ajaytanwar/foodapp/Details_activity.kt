package com.ajaytanwar.foodapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ajaytanwar.foodapp.databinding.ActivityDetailsBinding
import com.ajaytanwar.foodapp.moodle.CartItems
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Details_activity : AppCompatActivity() {

    private var foodname : String? =null
    private var foodImage : String? =null
    private var foodDescription : String? =null
    private var foodprice: String? =null
    private var foodIngredient : String? =null
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize Firebase Auth
        auth = Firebase.auth

        foodname = intent.getStringExtra("MenuItemName")
        foodprice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){

            detailsFood.text = foodname
            description.text=foodDescription
            ingredient.text= foodIngredient
            Glide.with(this@Details_activity).load(Uri.parse(foodImage)).into(detailFoodImage)


        }



        binding.imageButton.setOnClickListener{
            finish()
        }
        binding.addCartbutton.setOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""



        val cartItem =CartItems(foodname.toString(), foodprice.toString(), foodDescription.toString(), foodImage.toString(),1)
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Items added into cart successfully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Items Not added into cart ",Toast.LENGTH_SHORT).show()
        }

    }
}