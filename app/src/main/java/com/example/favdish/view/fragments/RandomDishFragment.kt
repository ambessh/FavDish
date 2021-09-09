package com.example.favdish.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.model.entities.RandomDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.example.favdish.viewmodel.NotificationsViewModel
import com.example.favdish.viewmodel.RandomDIshViewModel

class RandomDishFragment : Fragment() {


     var binding:FragmentRandomDishBinding?=null
    lateinit var mRandomDishViewModel:RandomDIshViewModel

    var dialog:Dialog?=null
    val  mFavDishViewModel:FavDishViewModel by viewModels{
        FavDishViewModelFactory( ((requireActivity().application) as FavDishApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentRandomDishBinding.inflate(inflater,container,false)
        return binding?.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog=Dialog(requireActivity())
        dialog?.let {

            dialog?.setContentView(R.layout.progress_bar)
            dialog?.show()
        }


        mRandomDishViewModel=ViewModelProvider(this).get(RandomDIshViewModel::class.java)

        mRandomDishViewModel.getRandomDishFromApi()

        binding!!.refreshLayout.setOnRefreshListener {
            mRandomDishViewModel.getRandomDishFromApi()
        }

        getActualResultApi()
    }

    fun getActualResultApi(){

       mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner,
           {responseData->
               responseData.let {
                  populateToUI(it)
                   if(binding!!.refreshLayout.isRefreshing){
                       binding!!.refreshLayout.isRefreshing=false
                   }


               }
           }
           )


        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner,
            {load->load?.let {
                Log.i("loadKlick",load.toString())
                dialog?.dismiss()

            }}
        )

        mRandomDishViewModel.loadRandomDishError.observe(viewLifecycleOwner,
            {dataError->
                dataError?.let {
                    Log.i("errorKlick",dataError.toString())
                }
            }
            )


    }

    fun populateToUI(recipe:RandomDish.Recipes){
        val myRecipe=recipe.recipes[0]
       Glide.with(requireActivity()).load(myRecipe.image).into(binding!!.imaged)
        binding!!.titled.text=myRecipe.title

        var dishType:String="other"
        if(dishType.isNotEmpty()){
            dishType=myRecipe.dishTypes[0]
        }
        binding!!.typed.text=dishType

        var ingredients=""

        for(value in myRecipe.extendedIngredients)
        if(ingredients.isEmpty()){
            ingredients=value.original
        }else{
            ingredients=ingredients+", \n"+value.original
        }

        binding!!.ing.text=ingredients

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            binding!!.dtc.text=Html.fromHtml(
                myRecipe.instructions,
               Html.FROM_HTML_MODE_COMPACT
            )
        }else{
            @Suppress("DEPERECATION")
            binding!!.dtc.text=Html.fromHtml(
                myRecipe.instructions
            )
        }

        binding!!.love.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_heart))

        binding!!.tame.text=resources.getString(R.string.COOKTAME,myRecipe.readyInMinutes.toString())

        var NotAddAgain=false

        binding!!.love.setOnClickListener(View.OnClickListener {

            if(NotAddAgain){
                Toast.makeText(requireActivity(),"Already added",Toast.LENGTH_SHORT).show()
            }else {
                val FavDishDetails: FavDish = FavDish(
                    myRecipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    myRecipe.title,
                    myRecipe.dishTypes[0],
                    "Other",
                    ingredients,
                    myRecipe.readyInMinutes.toString(),
                    myRecipe.instructions,
                    true
                )

                mFavDishViewModel.insert(FavDishDetails)

                Toast.makeText(requireActivity(), "Fav Dish added", Toast.LENGTH_SHORT).show()
                binding!!.love.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_love_red
                    )
                )

                NotAddAgain = true
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }
}