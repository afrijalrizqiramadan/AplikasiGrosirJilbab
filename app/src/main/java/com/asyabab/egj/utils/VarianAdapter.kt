package com.asyabab.egj.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.asyabab.egj.R
import com.asyabab.egj.ui.activity.MainActivity


internal class VarianAdapter(private var varianlist: List<String>, context: Context) :
    RecyclerView.Adapter<VarianAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: RadioButton = view.findViewById(R.id.radioBtn)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_varian, parent, false)
        this.context = context;

        return MyViewHolder(itemView)
    }

    private var mCheckedPostion = 0
    private var context: Context? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val varian = varianlist[position]
        holder.title.text = varian
        holder.title.isChecked = position === mCheckedPostion
        holder.title.setOnClickListener { v ->
            if (position === mCheckedPostion) {
                holder.title.isChecked = false
                mCheckedPostion = -1
            } else {
                mCheckedPostion = position
                notifyDataSetChanged()
            }
        }
        val intent = Intent(context, MainActivity::class.java)
        context!!.startActivity(intent)

    }

    override fun getItemCount(): Int {
        return varianlist.size
    }
}