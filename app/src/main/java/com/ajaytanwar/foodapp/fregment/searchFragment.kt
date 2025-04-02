package com.ajaytanwar.foodapp.fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.menuAdapter
import com.ajaytanwar.foodapp.databinding.FragmentSearchBinding
import com.ajaytanwar.foodapp.moodle.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class searchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuAdapter
    private lateinit var database: FirebaseDatabase
    private val orignalMenuItems = mutableListOf<MenuItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)


        retrieveMenuItem()


        //setup for search view
        setupSearchView()


        return binding.root




    }

    private fun retrieveMenuItem() {

        // get database reference
        database = FirebaseDatabase.getInstance()


        val foodReference: DatabaseReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {

                    val menuitem = foodSnapshot.getValue(MenuItem::class.java)
                    menuitem?.let {
                        orignalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(orignalMenuItems)
        setAdapter(filteredMenuItem)
    }


    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
        adapter = menuAdapter(filteredMenuItem, requireContext())
        binding.menuRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecycler.adapter = adapter

    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fiterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                fiterMenuItems(newText)
                return true
            }

        })
    }

    private fun fiterMenuItems(query: String) {
        val filteredMenuItem = orignalMenuItems.filter {
            it.foodname?.contains(query, ignoreCase = true) == true
        }

        setAdapter(filteredMenuItem)

    }

    companion object {

    }

}




