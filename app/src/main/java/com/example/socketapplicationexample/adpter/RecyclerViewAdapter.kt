package com.example.socketapplicationexample.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.socketapplicationexample.R
import com.example.socketapplicationexample.modelclass.NameValuePairs
import com.google.android.material.textview.MaterialTextView


class RecyclerViewAdapter(val context: Context, val model: ArrayList<NameValuePairs>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == THE_FIRST_VIEW) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.student_massage_view, parent, false)
            MyViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.teacher_massage_view, parent, false)
            MyViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == THE_FIRST_VIEW) {
            holder.itemView.findViewById<MaterialTextView>(R.id.studentsidemassage).text =
                model[position].message
        } else {
            holder.itemView.findViewById<MaterialTextView>(R.id.teachersidemassage).text =
                model[position].message
        }
    }

    override fun getItemCount(): Int = model.size
    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            THE_FIRST_VIEW
        } else {
            THE_SECOND_VIEW
        }
    }
}

