package com.example.mokkoji.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mokkoji.R
import com.example.mokkoji.datas.UserData
import com.example.mokkoji.utils.GlobalData

class UserRecyclerAdapter(
    val mContext : Context,
    val mList : List<UserData>
) : RecyclerView.Adapter<UserRecyclerAdapter.MyViewHolder>() {
    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind (item : UserData){
            val nickTxt = itemView.findViewById<TextView>(R.id.userNickTxt)

            nickTxt.text = item.nick_name

            val userImage = itemView.findViewById<ImageView>(R.id.userImage)

            Glide.with(mContext)
                .load(item.profileImg)
                .into(userImage)

            val invite = itemView.findViewById<Button>(R.id.inviteBtn)
            invite.setOnClickListener {
                
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}