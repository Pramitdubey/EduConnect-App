package com.example.educonnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.dataclass.User

class StudentRecyclerAdapter(
    private var mList: List<User>,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolderClass>()
{

    interface OnItemClickListener {
        fun onItemClick(teacher: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.student_items_look, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = mList[position]
        holder.studentEmail.text=currentItem.email
        holder.studentName.text = currentItem.name
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentEmail: TextView = itemView.findViewById(R.id.student_item_email)
        val studentName: TextView = itemView.findViewById(R.id.student_item_name)
    }

    fun updateData(newStudent: List<User>) {
        mList = newStudent
        notifyDataSetChanged()
    }
}
