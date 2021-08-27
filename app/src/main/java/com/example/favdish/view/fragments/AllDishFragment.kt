package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishBinding
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.adapters.AllDishesAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.HomeViewModel

class AllDishFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel
//    private var _binding: FragmentAllDishBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!

    private lateinit var binding:FragmentAllDishBinding



    val mFavDishViewModel:FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAllDish.layoutManager=GridLayoutManager(requireActivity(),2)
       val adapter=AllDishesAdapter(this@AllDishFragment)
        binding.rvAllDish.adapter=adapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes->
            dishes.let {
            if(it.isNotEmpty())
                binding.rvAllDish.visibility=View.VISIBLE
                binding.textHome.visibility=View.GONE
                adapter.dishesList(it)
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentAllDishBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addupdatedishmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_update_icon->
              startActivity(Intent(requireActivity(),AddUpdateDishActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}