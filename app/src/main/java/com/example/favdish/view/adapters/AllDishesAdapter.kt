package com.example.favdish.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favdish.databinding.AllDishItemBinding
import com.example.favdish.model.entities.FavDish

class AllDishesAdapter(
    private val fragment:Fragment,
):RecyclerView.Adapter<AllDishesAdapter.ViewHolder>() {

var dishes:List<FavDish> = listOf()

    class ViewHolder(view:AllDishItemBinding):RecyclerView.ViewHolder(view.root){
       val dishImage= view.dishimage
        val dishname=view.dishname
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDishesAdapter.ViewHolder {
        val binding:AllDishItemBinding= AllDishItemBinding.inflate(LayoutInflater.from(fragment.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllDishesAdapter.ViewHolder, position: Int) {
      val dish=dishes[position]

        Glide.with(fragment).load(dish.image).into(holder.dishImage)
        holder.dishname.text=dish.title
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list:List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }

}