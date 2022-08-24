package com.example.mokkoji.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.GroupActivity
import com.example.mokkoji.R
import com.example.mokkoji.datas.GroupData

class GroupRecyclerAdapter(
    val mContext: Context,
    val mList: List<GroupData>
) : RecyclerView.Adapter<GroupRecyclerAdapter.MyViewHolder>(){

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item : GroupData){
            val title = itemView.findViewById<TextView>(R.id.titleTxt)
            val groupExp = itemView.findViewById<TextView>(R.id.groupExpTxt)

            title.text = item.title
            groupExp.text = "총 인원 수 : " + item.groupTotal

            itemView.setOnClickListener {
                val myIntent = Intent(mContext, GroupActivity::class.java)
                mContext.startActivity(myIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return  mList.size
    }
}