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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val inputYear = customView.findViewById<EditText>(R.id.inputYearEdt)
            val inputMonth = customView.findViewById<EditText>(R.id.inputMonthEdt)
            val inputDateEdt = customView.findViewById<EditText>(R.id.inputDateEdt)
            val inputHourEdt = customView.findViewById<EditText>(R.id.inputHourEdt)
            val inputMinuetEdt = customView.findViewById<EditText>(R.id.inputMinuetEdt)

            val title = inputEdt.text.toString()
            val year = inputYear.text.toString()
            val month = inputMonth.text.toString()
            val date = inputDateEdt.text.toString()
            val hour = inputHourEdt.text.toString()
            val minuet = inputMinuetEdt.text.toString()

            val dateTime = year+month+date+hour+minuet

            if (title.isBlank() && year.isBlank() && month.isBlank() && date.isBlank() && hour.isBlank() && minuet.isBlank()){
                Toast.makeText(mContext, "공백이 있습니다", Toast.LENGTH_SHORT).show()
            }

            apiList.postRequestAddAppointment(
                token, title, dateTime, "place", 0, 0
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(mContext, "일정이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
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