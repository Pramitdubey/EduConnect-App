package com.example.educonnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.dataclass.Categories

class CategoriesRecyclerAdapter(var mList:List<Categories>,private val itemClickListener: OnItemClickListener):RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolderClass>() {

    interface OnItemClickListener {
        fun onItemClick(category: Categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.search_items_look,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem=mList[position]
        holder.rvImage.setImageResource(currentItem.image)
        holder.rvText.text=currentItem.title
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }

    }

    class ViewHolderClass(itemView:View):RecyclerView.ViewHolder(itemView) {
        val rvImage:ImageView=itemView.findViewById(R.id.logoIv)
        val rvText:TextView=itemView.findViewById(R.id.titleTv)

    }

    fun updateData(newCategories: List<Categories>) {
        mList = newCategories
        notifyDataSetChanged()
    }
}