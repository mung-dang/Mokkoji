package com.example.mokkoji.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mokkoji.GroupActivity
import com.example.mokkoji.MainActivity
import com.example.mokkoji.R
import com.example.mokkoji.api.APIList
import com.example.mokkoji.api.ServerAPI
import com.example.mokkoji.datas.AppointmentData
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.PlacesData
import com.example.mokkoji.fragments.GroupFragment
import com.example.mokkoji.fragments.ScheduleFragment
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*

class GroupRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlacesData>,
) : RecyclerView.Adapter<GroupRecyclerAdapter.MyViewHolder>(){
    val retrofit = ServerAPI.getRetrofit()
    val apiList = retrofit.create(APIList::class.java)
    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val mTodayList = ArrayList<AppointmentData>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item : PlacesData){
            val title = itemView.findViewById<TextView>(R.id.titleTxt)
            val news = itemView.findViewById<ImageView>(R.id.news)
            val groupSchNum = itemView.findViewById<TextView>(R.id.groupSchTxt)

            title.text = item.title

            val now = Calendar.getInstance()
            val token = ContextUtil.getLoginToken(mContext)
            apiList.getRequestAppointment(token).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if(response.isSuccessful){
                        mTodayList.clear()

                        val br = response.body()!!
                        val appointmentList = br.data.appointments

                        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val sdf = SimpleDateFormat("yyyy-MM-dd")

                        for (appointment in appointmentList) {
                            val dateTime = formatter.parse(appointment.datetime)

                            if (
                                appointment.place == item.title
                                && sdf.format(dateTime) == sdf.format(now.time)
                            ) {
                                mTodayList.add(appointment)
                            }
                        }
                        if(mTodayList.size == 0){
                            groupSchNum.text = "????????? ????????? ????????????"
                            news.visibility = View.GONE
                        }else{
                            groupSchNum.text = "????????? ?????? : " + mTodayList.size.toString()
                            news.visibility = View.VISIBLE
                        }

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })



            itemView.setOnClickListener {
                val myIntent = Intent(mContext, GroupActivity::class.java)
                mContext.startActivity(myIntent)
                GlobalData.groupTitle = item.title
            }

            itemView.setOnLongClickListener {
                val token = ContextUtil.getLoginToken(mContext)
                val groupId = item.id
                val alert = AlertDialog.Builder(mContext)
                    .setMessage("?????? ?????????????????????????")
                    .setPositiveButton("????????????", DialogInterface.OnClickListener { dialogInterface, i ->
                        apiList.deleteRequestGroup(token, groupId).enqueue(object : Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){
                                    val br = response.body()!!
                                    Toast.makeText(mContext, "?????????????????????.", Toast.LENGTH_SHORT).show()
                                    ((mContext as MainActivity).supportFragmentManager.findFragmentByTag("f0") as GroupFragment).getGroupDataFromServer()
                                    database.getReference("data").child(item.title).removeValue()
                                }
                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                            }

                        })
                    })
                    .setNegativeButton("??????", null)
                    .show()


                true
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