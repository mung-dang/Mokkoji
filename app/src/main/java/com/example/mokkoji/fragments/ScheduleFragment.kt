package com.example.mokkoji.fragments

import android.os.Bundle

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
import java.util.*
import kotlin.collections.ArrayList

class ScheduleFragment : BaseFragment() {

    lateinit var binding: FragmentScheduleBinding
    lateinit var mScheduleAdapter : ScheduleRecyclerAdapter
    val mScheduleList = ArrayList<AppointmentData>()
    val now = Calendar.getInstance()

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

        val calendar = Calendar.getInstance()
        val today = calendar.time
        val sdf = SimpleDateFormat("M/d")
        val finalToday = sdf.format(today)
        binding.today.text = finalToday

        binding.groupCalendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.today.text = sdf.format(date.time)

            getAppointmentFromServer(date.time)
        }

        binding.selectCalendar.setOnClickListener {
            if(binding.calendarLayout.visibility == View.GONE){
                binding.expand.setImageResource(R.drawable.ic_baseline_expand_more_24)
                binding.calendarLayout.visibility = View.VISIBLE
                return@setOnClickListener
            }
            else{
                binding.expand.setImageResource(R.drawable.ic_baseline_expand_less_24)
                binding.calendarLayout.visibility = View.GONE
                return@setOnClickListener
            }

        }
    }

    override fun setValues() {
        mScheduleAdapter = ScheduleRecyclerAdapter(mContext, mScheduleList)
        binding.planRecyclerView.adapter = mScheduleAdapter
        binding.planRecyclerView.layoutManager = LinearLayoutManager(mContext)
        getAppointmentFromServer(now.time)
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

            val mSelectedDate = Calendar.getInstance()

            mSelectedDate.set(
                datePick.year, datePick.month, datePick.dayOfMonth, timePick.currentHour, timePick.currentMinute
            )

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateTime = sdf.format(mSelectedDate.time)

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
                        Toast.makeText(mContext, "일정이 성공적으로 추가되었습니다", Toast.LENGTH_SHORT).show()

                        getAppointmentFromServer(mSelectedDate.time)

                        //약속이 추가 된 후 달력표시도 변경?
                        val sdf = SimpleDateFormat("M/d")
                        binding.today.text = sdf.format(mSelectedDate.time)
                        binding.groupCalendar.date = mSelectedDate.timeInMillis

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

    fun getAppointmentFromServer(date : Date){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestAppointment(token).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mScheduleList.clear()

                    val br = response.body()!!
                    val appointmentList = br.data.appointments

                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val sdf = SimpleDateFormat("yyyy-MM-dd")

                    for (appointment in appointmentList) {
                        val dateTime = formatter.parse(appointment.datetime)
                        if (
                            appointment.place == GlobalData.groupTitle
                            && sdf.format(dateTime) == sdf.format(date)
                        ) {
                            mScheduleList.add(appointment)
                        }
                    }

                    mScheduleAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}