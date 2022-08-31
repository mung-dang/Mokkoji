package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.R
import com.example.mokkoji.adapters.ScheduleRecyclerAdapter
import com.example.mokkoji.databinding.FragmentScheduleBinding
import com.example.mokkoji.datas.AppointmentData
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : BaseFragment() {

    lateinit var binding: FragmentScheduleBinding
    lateinit var mScheduleAdapter : ScheduleRecyclerAdapter
    val mScheduleList = ArrayList<AppointmentData>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.addBtn.setOnClickListener {
            getAddAppointment()
        }

    }

    override fun setValues() {
        mScheduleAdapter = ScheduleRecyclerAdapter(mContext, mScheduleList)
        binding.planRecyclerView.adapter = mScheduleAdapter
        binding.planRecyclerView.layoutManager = LinearLayoutManager(mContext)

        getAppointmentFromServer()

    }

    fun getAddAppointment(){
        val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog_schedule, null)
        val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
        val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)

        val alert = AlertDialog.Builder(mContext)
            .setMessage("일정 추가하기")
            .setView(customView)
            .create()

        positiveBtn.setOnClickListener {
            val token = ContextUtil.getLoginToken(mContext)
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            val title = inputEdt.text.toString()
            val datePick = customView.findViewById<DatePicker>(R.id.datePick)
            val timePick = customView.findViewById<TimePicker>(R.id.timePick)
            val groupTitle = GlobalData.groupTitle.toString()

            val now = Calendar.getInstance()

            now.set(
                datePick.year, datePick.month, datePick.dayOfMonth, timePick.currentHour, timePick.currentMinute
            )

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateTime = sdf.format(now.time)

            val currentDay = System.currentTimeMillis()
            val currentDate = Date(currentDay)
            val today = sdf.format(currentDate)

            if (title.isBlank()){
                Toast.makeText(mContext, "일정 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateTime <= today){
                Toast.makeText(mContext, "현재 이후의 일정을 등록해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestAddAppointment(
                token, title, dateTime, groupTitle, 0, 0
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(mContext, "일정이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        getAppointmentFromServer()
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

    fun getAppointmentFromServer(){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestAppointment(token).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mScheduleList.clear()

                    val br = response.body()!!
                    mScheduleList.addAll(br.data.appointments)
                    mScheduleAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}