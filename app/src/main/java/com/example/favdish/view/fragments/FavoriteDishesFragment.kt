package com.example.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.application.FavDishApplication

import com.example.favdish.databinding.FragmentFavoriteDishesBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.activities.MainActivity
import com.example.favdish.view.adapters.AllDishesAdapter
import com.example.favdish.viewmodel.DashboardViewModel
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

//    private lateinit var dashboardViewModel: DashboardViewModel
//    private var _binding: FragmentFavoriteDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

     var binding:FragmentFavoriteDishesBinding? = null


    val mViewModel:FavDishViewModel by viewModels {
       FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
//
//        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
        binding=FragmentFavoriteDishesBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.rvLDish.layoutManager=GridLayoutManager(requireActivity(),2)
        val adapter=AllDishesAdapter(this@FavoriteDishesFragment)
        binding!!.rvLDish.adapter=adapter

        mViewModel.getLoveDishNow.observe(viewLifecycleOwner){
            it.let {
                if(it.isNotEmpty()){
                    Log.e("TAGG",it.toString())
                    binding!!.tvL.visibility=View.GONE
                    binding!!.rvLDish.visibility=View.VISIBLE
                    adapter.dishesList(it)
                }else{
                    binding!!.tvL.visibility=View.VISIBLE
                    binding!!.rvLDish.visibility=View.GONE
                }


            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        binding=null
    }

    fun navigationHandlerLove(favDish: FavDish){
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavBar()
        }
        findNavController().navigate(FavoriteDishesFragmentDirections.fromFavDishToDetails(favDish))
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }
}