package com.ajaytanwar.foodapp.Adapter
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ajaytanwar.foodapp.databinding.CartItemsBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices:MutableList<String>,
    private val cartIngredient: MutableList<String>,
    private val cartImage:MutableList<String>,
    private val cartQuantity: MutableList<Int>,
    private val cartDescription: MutableList<String>

         ) : RecyclerView.Adapter<cartAdapter.CartViewHolder>(){

               //instance Firebase
       private val auth  = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemsReference = database.reference.child("user").child(userId).child("CartItems")

    }
         companion object {
             private var itemQuantities: IntArray = intArrayOf()
             private lateinit var cartItemsReference: DatabaseReference

         }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int = cartItems.size

    //get updated quantity

    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity

    }

    inner class CartViewHolder (private val binding: CartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartfoodname.text = cartItems[position]
                cartitemprice.text = cartItemPrices[position]

                // load image using Glide

                val uriString = cartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartimage)
                cartItemQunatity.text = quantity.toString()

                minusbutton.setOnClickListener {
                    deceaseQunatity(position)

                }

                plusbutton.setOnClickListener {
                    ineceaseQunatity(position)

                }

                deletebutton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteitem(itemPosition)
                    }

                }

            }

        }


        private fun ineceaseQunatity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemQunatity.text = itemQuantities[position].toString()
            }

        }

        private fun deceaseQunatity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartQuantity[position] = itemQuantities[position]
                binding.cartItemQunatity.text = itemQuantities[position].toString()
            }

        }

        private fun deleteitem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                cartItems.removeAt(position)
                cartImage.removeAt(position)
                cartDescription.removeAt(position)
                cartItemPrices.removeAt(position)
                cartQuantity.removeAt(position)
                cartIngredient.removeAt(position)
                Toast.makeText(context, "Item delete", Toast.LENGTH_SHORT).show()

                //update item quantities
                itemQuantities =
                    itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)

            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) -> Unit) {
        cartItemsReference.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey: String? = null

                //loop for snapshot children
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index  == positionRetrieve){
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
