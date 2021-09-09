package com.example.favdish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.AllDishItemBinding
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.databinding.FragmentAllDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.activities.MainActivity
import com.example.favdish.view.adapters.AllDishesAdapter
import com.example.favdish.view.adapters.CustomListItemAdapter
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

    private  var binding:FragmentAllDishBinding?=null

private lateinit var dialog:Dialog
 private lateinit var adapter:AllDishesAdapter

    val mFavDishViewModel:FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvAllDish?.layoutManager =GridLayoutManager(requireActivity(),2)
        adapter=AllDishesAdapter(this@AllDishFragment)
        binding?.rvAllDish?.adapter =adapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes->
            dishes.let {
            if(it.isNotEmpty()) {
                binding?.rvAllDish?.visibility = View.VISIBLE
                binding?.textHome?.visibility = View.GONE
                adapter.dishesList(it)
            }
            }

        }


    }

    fun navigationHandlerDetail(favDish:FavDish){
     if(requireActivity() is MainActivity){
         (activity as MainActivity?)?.hideBottomNavBar()
     }
     findNavController().navigate(AllDishFragmentDirections.fromAllDishToDishDetails(
         favDish
     ))
    }


    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavBar()
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
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
     binding=null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addupdatedishmenu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_update_icon-> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
           R.id.ic_filter->{
              ShowTypeList()
               return true
           }
            else->{
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun delete(favDish: FavDish){
    val builder=AlertDialog.Builder(this.context)
        builder.setMessage("Do you want to delete this dish?")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            .setPositiveButton("Delete!", DialogInterface.OnClickListener { dialog, which ->
                mFavDishViewModel.deleteDishNow(favDish)
                Toast.makeText(requireActivity(),"Dish delete sucess!",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            })

        val dialog=builder.create()
        dialog.show()


    }

    fun ShowTypeList(){
         dialog=Dialog(requireActivity())

        val binding=DialogCustomListBinding.inflate(layoutInflater)
        dialog.setContentView(binding!!.root)

        val myList:ArrayList<String> =Constants.dishTypes()

        myList.add(0,resources.getString(R.string.All_SHOW))

        val adapter=CustomListItemAdapter(requireActivity(),this, myList,Constants.DISH_TYPE)

        binding?.rvItem?.layoutManager=LinearLayoutManager(requireActivity())
        binding?.tvSelect?.text=resources.getString(R.string.filter)
        binding?.rvItem?.adapter=adapter
        dialog.show()


    }

    fun filterNow(item:String,selection:String){
        if(selection==Constants.DISH_TYPE){
            if(item==resources.getString(R.string.All_SHOW)){

                mFavDishViewModel.allDishesList.observe(viewLifecycleOwner){

                 it.let {
                     if(it.isNotEmpty()){
                         binding?.textHome?.visibility=View.GONE
                         binding?.rvAllDish?.visibility=View.VISIBLE
                         adapter.dishesList(it)
                     }
                 }
                }
            }else {
                Log.e("know",item.toString())
                  item.let {
                      if(it.isNotEmpty()){
                          mFavDishViewModel.getFilteredNow(it).observe(viewLifecycleOwner){
                              if(it.isNotEmpty()){
                                  binding?.textHome?.visibility=View.GONE
                                  binding?.rvAllDish?.visibility=View.VISIBLE
                                  adapter.dishesList(it)
                              }else{
                                  binding?.textHome?.visibility=View.VISIBLE
                                  binding?.rvAllDish?.visibility=View.GONE
                              }
                          }
                      }
                  }
            }
            dialog.dismiss()
        }
    }
}