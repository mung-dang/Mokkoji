package com.example.mokkoji.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.R
import com.example.mokkoji.databinding.PostListItemBinding
import com.example.mokkoji.datas.PostData
import com.example.mokkoji.utils.ContextUtil

class PostRecyclerAdapter(
    val mContext: Context,
    val mList : List<PostData>
) : RecyclerView.Adapter<PostRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(item : PostData){
            val title = itemView.findViewById<TextView>(R.id.titleTxt)
            val content = itemView.findViewById<TextView>(R.id.contentTxt)
            val date = itemView.findViewById<TextView>(R.id.dateTxt)

            title.text = item.title
            content.text = item.content
            date.text = item.date

            if (item.deviceToken == ContextUtil.getDeviceToken(mContext)){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.post_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}