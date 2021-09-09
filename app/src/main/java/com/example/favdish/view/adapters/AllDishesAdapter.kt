package com.example.favdish.view.adapters

import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.databinding.AllDishItemBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.activities.AddUpdateDishActivity
import com.example.favdish.view.fragments.AllDishFragment
import com.example.favdish.view.fragments.FavoriteDishesFragment
import com.example.favdish.viewmodel.FavDishViewModel

class AllDishesAdapter(
    private val fragment:Fragment,
):RecyclerView.Adapter<AllDishesAdapter.ViewHolder>() {

var dishes:List<FavDish> = listOf()

    class ViewHolder(view:AllDishItemBinding):RecyclerView.ViewHolder(view.root){
       val dishImage= view.dishimage
        val dishname=view.dishname

        val ivMore=view.ivMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDishesAdapter.ViewHolder {
        val binding:AllDishItemBinding= AllDishItemBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllDishesAdapter.ViewHolder, position: Int) {
      val dish=dishes[position]

        Glide.with(fragment).load(dish.image).into(holder.dishImage)
        holder.dishname.text=dish.title
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(fragment is AllDishFragment){
                    fragment.navigationHandlerDetail(dish)
                }else if(fragment is FavoriteDishesFragment){
                    fragment.navigationHandlerLove(dish)
                }
            }

        })

        holder.ivMore.setOnClickListener(View.OnClickListener {
             val popUp=PopupMenu(it.context,holder.ivMore)
             popUp.menuInflater.inflate(R.menu.menu_more,popUp.menu)

            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.edit_more->{
                        Edit(dish)
                    }
                    R.id.delete_more->{
                        Delete(dish)
                    }

                }
                true
            }
            popUp.show()

        })

        if( fragment is AllDishFragment){

            holder.ivMore.visibility=View.VISIBLE
        }else if(fragment is FavoriteDishesFragment){
            holder.ivMore.visibility=View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list:List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }

    fun Edit(dish:FavDish){
     val intent=Intent(fragment.requireActivity(),AddUpdateDishActivity::class.java)
     intent.putExtra(Constants.EDIT_DISH,dish)
     fragment.requireActivity().startActivity(intent)
    }

    fun Delete(favDish: FavDish){
        if(fragment is AllDishFragment){
            fragment.delete(favDish)
        }
    }

}