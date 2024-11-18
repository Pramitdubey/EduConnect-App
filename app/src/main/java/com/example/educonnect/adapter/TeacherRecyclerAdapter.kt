package com.example.educonnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.dataclass.Categories
import com.example.educonnect.dataclass.User

class TeacherRecyclerAdapter(
    private var mList: List<User>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TeacherRecyclerAdapter.ViewHolderClass>() {

    interface OnItemClickListener {
        fun onItemClick(teacher: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.teachers_items_look, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = mList[position]
        holder.teacherEmail.text=currentItem.email
        holder.teacherName.text = currentItem.name
        holder.teacherSubject.text = currentItem.subject
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(currentItem)
        }
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teacherEmail: TextView = itemView.findViewById(R.id.teacher_item_email)
        val teacherName: TextView = itemView.findViewById(R.id.teacher_item_name)
        val teacherSubject: TextView = itemView.findViewById(R.id.teacher_item_subject)
    }

    fun updateData(newTeacher: List<User>) {
        mList = newTeacher
        notifyDataSetChanged()
    }

}
