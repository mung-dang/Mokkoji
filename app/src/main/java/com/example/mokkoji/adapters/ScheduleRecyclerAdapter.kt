package com.example.mokkoji.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
            val dayTime = SimpleDateFormat("yy-MM-dd   HH:mm")

            val dateTime = formatter.parse(item.datetime)
            val finalDate = sdf.format(dateTime)
            val finalTDate = sdf.format(date)
            val title = itemView.findViewById<TextView>(R.id.title)
            val date = itemView.findViewById<TextView>(R.id.date)

            if(item.place == GlobalData.groupTitle){
                if(finalDate.equals(finalTDate)){
                    val day = formatter.parse(item.datetime)
                    val tDay = dayTime.format(day)
                    title.text = item.title
                    date.text = tDay
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

            itemView.setOnClickListener {
                val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog_schedule, null)
                val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
                val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)
                val alert = AlertDialog.Builder(mContext)
                    .setView(customView)
                    .setMessage("일정을 수정하시겠습니까?")
                    .create()
                positiveBtn.text = "수정하기"
                negativeBtn.text = "취소"
                positiveBtn.setOnClickListener {
                    val token = ContextUtil.getLoginToken(mContext)
                    val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
                    val title = inputEdt.text.toString()

                    val datePick = customView.findViewById<DatePicker>(R.id.datePick)
                    val timePick = customView.findViewById<TimePicker>(R.id.timePick)
                    val now = Calendar.getInstance()

                    now.set(
                        datePick.year, datePick.month, datePick.dayOfMonth, timePick.currentHour, timePick.currentMinute
                    )
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                    val dateTime = sdf.format(now.time)

                    val currentDay = System.currentTimeMillis()
                    val currentDate = Date(currentDay)
                    val today = sdf.format(currentDate)

                    if (item.title.isBlank()){
                        Toast.makeText(mContext, "일정 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (item.datetime <= today){
                        Toast.makeText(mContext, "현재 이후의 일정을 등록해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    apiList.putRequestReAppointment(token, item.id, title, dateTime, item.place, 0, 0).enqueue(object : Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(mContext, "일정이 변경되었습니다", Toast.LENGTH_SHORT).show()
                                ((mContext as GroupActivity).supportFragmentManager.findFragmentByTag("f1") as ScheduleFragment).getAppointmentFromServer()
                                alert.dismiss()

                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                    })
                }
                negativeBtn.setOnClickListener {
                    alert.dismiss()
                }
                alert.show()
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