package com.example.mokkoji.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.R
import com.example.mokkoji.datas.AppointmentData
import java.text.SimpleDateFormat

class MonthRecyclerAdapter(
    val mContext: Context,
    val mList : List<AppointmentData>
) : RecyclerView.Adapter<MonthRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(item: AppointmentData){
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dayTime = SimpleDateFormat("d일 E요일")

            val dayTxt = itemView.findViewById<TextView>(R.id.dayTxt)
            val contentTxt = itemView.findViewById<TextView>(R.id.contentTxt)

            val dayFormat = formatter.parse(item.datetime)
            val day = dayTime.format(dayFormat)

            dayTxt.text = day
            contentTxt.text = item.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.month_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return 4
    }


}