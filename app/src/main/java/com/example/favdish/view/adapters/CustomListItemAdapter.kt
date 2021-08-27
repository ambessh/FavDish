package com.example.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favdish.databinding.ItemCustomListBinding
import com.example.favdish.view.activities.AddUpdateDishActivity

class CustomListItemAdapter(private val activity:Activity,
                            private val listItems:List<String>,
                            private val selection:String
                            ) :RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    class ViewHolder(view:ItemCustomListBinding):RecyclerView.ViewHolder(view.root){
        val tvText=view.tvItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomListItemAdapter.ViewHolder {
       val binding:ItemCustomListBinding= ItemCustomListBinding.inflate(LayoutInflater.from(activity),parent,false)
       return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomListItemAdapter.ViewHolder, position: Int) {
        val item=listItems[position]
        holder.tvText.text=item
        holder.itemView.setOnClickListener{
            if(activity is AddUpdateDishActivity){
                activity.selectedItem(item,selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }


}