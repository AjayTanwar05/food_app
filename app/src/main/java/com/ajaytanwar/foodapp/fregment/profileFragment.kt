package com.ajaytanwar.foodapp.fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ajaytanwar.foodapp.databinding.FragmentProfileBinding
import com.ajaytanwar.foodapp.moodle.UserMoodle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class profileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUserData()

        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val password = binding.password.text.toString()
            val phone = binding.phone.text.toString()


            updateUserData(name, email, address,password, phone)
        }

        binding.name.isEnabled = false
        binding.email.isEnabled = false
        binding.address.isEnabled = false
        binding.password.isEnabled = false
        binding.phone.isEnabled = false


        var isEnable = false

        binding.EditProfile.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.phone.isEnabled = isEnable

        }

        return binding.root

    }



    private fun updateUserData(name: String, email: String, address: String,password :String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId !=null){
            val userReference = database.getReference("user").child(userId)

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "password" to password,
                "phone" to phone




            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Update Successfully😊",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Profile Update Failed🥲",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val  userId = auth.currentUser?.uid
        if (userId!=null){
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile  = snapshot.getValue(UserMoodle::class.java)
                        if (userProfile != null){
                            binding.name.setText(userProfile.name)
                            binding.address.setText(userProfile.address)
                            binding.email.setText(userProfile.email)
                            binding.password.setText(userProfile.password)
                            binding.phone.setText(userProfile.phone)


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