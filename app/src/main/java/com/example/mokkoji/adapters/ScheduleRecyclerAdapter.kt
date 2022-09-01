package com.example.mokkoji.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.GroupActivity
import com.example.mokkoji.R
import com.example.mokkoji.api.APIList
import com.example.mokkoji.api.ServerAPI
import com.example.mokkoji.datas.AppointmentData
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.fragments.ScheduleFragment
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleRecyclerAdapter(
    val mContext : Context,
    val mList : List<AppointmentData>
) : RecyclerView.Adapter<ScheduleRecyclerAdapter.MyViewHolder>() {
    val retrofit = ServerAPI.getRetrofit()
    val apiList = retrofit.create(APIList::class.java)
    var date : Date = Calendar.getInstance().time

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: AppointmentData) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateTime = formatter.parse(item.datetime)
            val finalDate = sdf.format(dateTime)
            val finalTDate = sdf.format(date)
            val title = itemView.findViewById<TextView>(R.id.title)
            val date = itemView.findViewById<TextView>(R.id.date)
            val news = itemView.findViewById<TextView>(R.id.news)

            if(item.place == GlobalData.groupTitle){


                if(finalDate.equals(finalTDate)){
                    title.text = item.title
                    date.text = item.datetime
                }else{
                    title.text = null
                    date.text = null
                }
            }else{
                title.text = null
                date.text = null
            }

            itemView.setOnLongClickListener {
                val token = ContextUtil.getLoginToken(mContext)
                val scheduleId = item.id
                val alert = AlertDialog.Builder(mContext)
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제하기", DialogInterface.OnClickListener { dialogInterface, i ->
                        apiList.deleteRequestAppointment(token, scheduleId).enqueue(object :
                            Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!
                                    Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                    ((mContext as GroupActivity).supportFragmentManager.findFragmentByTag("f1") as ScheduleFragment).getAppointmentFromServer()
                                }
                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                            }

                        })
                    })
                    .setNegativeButton("취소", null)
                    .show()


                true
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.schedule_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}