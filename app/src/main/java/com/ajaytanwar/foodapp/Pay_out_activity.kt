package com.ajaytanwar.foodapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajaytanwar.foodapp.databinding.ActivityPayOutBinding
import com.ajaytanwar.foodapp.moodle.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Pay_out_activity : AppCompatActivity() {
    lateinit var binding: ActivityPayOutBinding


    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var totalamount : String
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var FoodItemQuantites: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initiate Firebase and user detail
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()


        // set user data
        setUserData()






        // get user detail from firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        FoodItemQuantites = intent.getIntegerArrayListExtra("FoodItemQuantites") as ArrayList<Int>


        totalamount = calculateTotalAmount().toString() + "₹"
        // binding.totalAmount.isEnabled = false
        binding.totalAmount.setText(totalamount)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.PlaceMyOrder.setOnClickListener {

            // get data from textView

            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if (name.isBlank() || address.isBlank() || phone.isBlank()) {

                Toast.makeText(this, "Please Enter All the Detail", Toast.LENGTH_SHORT).show()


            } else {
                placeOrder()
            }


        }

    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until FoodItemQuantites.size){

            val price = foodItemPrice[i]
            val lastChar = price.last()
            val priceInVelue = if (lastChar == '₹') {
                price.dropLast(1).toInt()

            } else {
                price.toInt()
            }

            var quantity = FoodItemQuantites[i]
            totalAmount += priceInVelue * quantity

        }
        return totalAmount

    }


    private fun placeOrder() {
        userId = auth.currentUser?.uid ?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId, name, foodItemName, foodItemPrice, foodItemImage,FoodItemQuantites, address , totalamount , phone, time, itemPushKey, false, false
        )
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)

        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialog = congrestButtomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)

        }
    }


    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
            }

    }


    private fun removeItemFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }


    private fun setUserData() {
        val user = auth.currentUser
        if (user!=null){
            val userId = user.uid
            val userReference = databaseReference.child("user").child(userId)

            userReference.addListenerForSingleValueEvent(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresses = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            name.setText(names)
                            address.setText(addresses)
                            phone.setText(phones)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}