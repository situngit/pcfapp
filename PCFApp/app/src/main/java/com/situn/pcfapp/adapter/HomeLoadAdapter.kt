package com.situn.pcfapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.situn.pcfapp.R
import com.situn.pcfapp.activity.FullScreenDataActivity
import com.situn.pcfapp.databinding.RecyclerRowBinding
import com.situn.pcfapp.model.HomeModel

class HomeLoadAdapter(val homeDataList: ArrayList<HomeModel>) : RecyclerView.Adapter<HomeLoadAdapter.ViewHolder>(){

    lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return homeDataList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeValue: HomeModel = homeDataList[position]

        Glide.with(context).load(homeValue.image_avatar)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageMain);

        holder.tvId.setText(homeValue.id.toString())
        holder.tvName.setText(homeValue.name)
        holder.tvFullName.setText(homeValue.full_name)

        // Set height and width dynamically


        holder.llRow.setOnClickListener{
            val intent = Intent(holder.itemView.context, FullScreenDataActivity::class.java)
            intent.putExtra("ImageUrl", homeValue.image_avatar)
            intent.putExtra("Id", homeValue.id.toString())
            intent.putExtra("Name", homeValue.name)
            intent.putExtra("FullName", homeValue.full_name)
            intent.putExtra("Url", homeValue.url)
            holder.itemView.context.startActivity(intent)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val llRow = itemView.findViewById(R.id.ll_row) as LinearLayout
        val imageMain = itemView.findViewById(R.id.iv_image) as ImageView
        val tvId = itemView.findViewById(R.id.tv_id) as TextView
        val tvName = itemView.findViewById(R.id.tv_name) as TextView
        val tvFullName = itemView.findViewById(R.id.tv_full_name) as TextView
    }
}