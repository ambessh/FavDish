package com.example.favdish.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentDishDetailsBinding
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DishDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 *
 */

lateinit var binding:FragmentDishDetailsBinding

class DishDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_icon,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share_icon->{
                shareToMedia()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun shareToMedia(){
        val args:DishDetailsFragmentArgs by navArgs()

        args?.data.let {
            val intent=Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT,"Checkout this recipe!")
            intent.putExtra(Intent.EXTRA_TEXT,it?.image.toString())
            startActivity(Intent.createChooser(intent,"Share With"))

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= FragmentDishDetailsBinding.inflate(inflater,container,false)
        return binding.root
        // Inflate the layout for this fragment
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFavDishViewModel:FavDishViewModel by viewModels{
            FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
        }
        val args:DishDetailsFragmentArgs by navArgs()
       args.let {

           Glide.with(this).load(it.data?.image)
               .listener(object:RequestListener<Drawable>{
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       Log.e("Tag","error",e)
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       resource.let {
                           Palette.from(resource!!.toBitmap())
                               .generate(){
                                   it.let {
                                       val colorInt=it?.vibrantSwatch?.rgb?:0
                                       binding.dll.setBackgroundColor(colorInt)
                                   }
                               }
                       }
                     return false
                   }

               })
               .into(binding.imaged)
           binding.titled.text=it.data?.title
           binding.typed.text=it.data?.type
           binding.categ.text=it.data?.category
           binding.ing.text=it.data?.ingredients
           binding.dtc.text=it.data?.directionToCook
           binding.tame.text= resources.getString(R.string.COOKTAME,it.data?.cookingTime)

           if(args.data?.favouriteDish!!){
               binding.love.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_love_red))
           }
           else{
               binding.love.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_heart))

           }

       }

        binding.love.setOnClickListener {

            val value: Boolean = !args.data?.favouriteDish!!
            args.data?.favouriteDish = value

            mFavDishViewModel.updateData(args.data!!)

            if(args.data?.favouriteDish!!){
                binding.love.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_love_red))
            }
            else{
                binding.love.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_heart))

            }

        }



    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DishDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DishDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}