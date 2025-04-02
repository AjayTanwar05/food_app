package com.ajaytanwar.foodapp.fregment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.foodapp.Adapter.menuAdapter
import com.ajaytanwar.foodapp.R
import com.ajaytanwar.foodapp.databinding.FragmentHomeBinding
import com.ajaytanwar.foodapp.menubottomsheetFragment
import com.ajaytanwar.foodapp.moodle.MenuItem
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems :MutableList<MenuItem>

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewallmenu.setOnClickListener {
          val bottomSheetDialog = menubottomsheetFragment()
          bottomSheetDialog.show(parentFragmentManager,"Test")
        }

        //Retrieve  and display popular items
        retriveandDisplaypopularItems()
        return binding.root

    }

    private fun retriveandDisplaypopularItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        //retrieve menu items from the database
        foodRef.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it)}
                }
                rendomItempopuler()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun rendomItempopuler() {
        val intex = menuItems.indices.toList().shuffled()
        val  numItemToShow =  7
        val subsetMenuItem = intex.take(numItemToShow).map { menuItems[it] }

        setPopulerItemsAdapter(subsetMenuItem)

    }

    private fun setPopulerItemsAdapter(subsetMenuItem: List<MenuItem>) {
        val adapter = menuAdapter(subsetMenuItem,requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager( requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imagelist = ArrayList<SlideModel>()
        imagelist.add(SlideModel(R.drawable.baner1, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.baner2, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.baner3, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.baner4, ScaleTypes.FIT))
        imagelist.add(SlideModel(R.drawable.baner7, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imagelist)
        imageSlider.setImageList(imagelist,ScaleTypes.FIT)


        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imagelist [ position]
                val itemMessage = "selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })

    }

}