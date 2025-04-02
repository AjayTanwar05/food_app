package com.ajaytanwar.foodapp.fregment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.cartAdapter
import com.ajaytanwar.foodapp.Pay_out_activity
import com.ajaytanwar.foodapp.databinding.FragmentCartBinding
import com.ajaytanwar.foodapp.moodle.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding : FragmentCartBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
   private lateinit var foodName : MutableList<String>
   private lateinit var foodprice : MutableList<String>
   private lateinit var foodDescription : MutableList<String>
   private lateinit var foodImageUri : MutableList<String>
   private lateinit var foodIngredient : MutableList<String>
   private lateinit var foodQuantity: MutableList<Int>
   private lateinit var cartAdapter: cartAdapter
   private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      binding = FragmentCartBinding.inflate(inflater,container,false)



        auth = FirebaseAuth.getInstance()
        retriveCartItmes()


        binding.proceedButton.setOnClickListener{

            //get item detail before
            getOrderItemDetails()

        }

        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference = database.reference.child("user").child(userId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodprices = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        // get item quantity
         val foodQuantity = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot in snapshot.children){
                    //get the cartItems to respective list
                    val  orderItems = foodSnapshot.getValue(CartItems::class.java)

                    //add items details in to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodprice?.let { foodprices.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }

                }
                orderNow(foodName,foodprices,foodDescription,foodImage,foodIngredient,foodQuantity)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Order making Failed , Please Try Again",Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun orderNow(
        foodName: MutableList<String>,
        foodprice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantity: MutableList<Int>
    ) {

        if (isAdded && context!= null){
            val intent = Intent(requireContext(),Pay_out_activity::class.java)
            intent.putExtra("FoodItemName",foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice",foodprice as ArrayList<String>)
            intent.putExtra("FoodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantites",foodQuantity as ArrayList<Int>)

            startActivity(intent)
        }
    }

    private fun retriveCartItmes() {

        // database reference to the firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val foodReference : DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        // list to tor cart item

        foodName = mutableListOf()
        foodprice = mutableListOf()
        foodIngredient = mutableListOf()
        foodDescription = mutableListOf()
        foodQuantity = mutableListOf()
        foodImageUri = mutableListOf()

        // fetch data from the database
        foodReference.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){

                    //get the cart item
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    //add cart item detail to the list

                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodprice?.let { foodprice.add(it) }
                    cartItems?.foodDescription?.let { foodDescription.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodQuantity?.let { foodQuantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredient.add(it) }

                }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = cartAdapter(requireContext(),foodName,foodprice,foodDescription,foodImageUri,foodQuantity,foodIngredient)
                binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter


            }

            override fun onCancelled(error: DatabaseError) {
             Toast.makeText(context,"data not fetch",Toast.LENGTH_SHORT).show()
            }

        })
    }

}