package com.example.mokkoji.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.R
import com.example.mokkoji.UserActivity
import com.example.mokkoji.adapters.MonthRecyclerAdapter
import com.example.mokkoji.databinding.FragmentHomeBinding
import com.example.mokkoji.datas.AppointmentData
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var mMonthAdapter: MonthRecyclerAdapter
    val mMonthList = ArrayList<AppointmentData>()
    val now = Calendar.getInstance()

    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        database.getReference("data").child("goal").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.groupExpTxt.text = snapshot.child("${GlobalData.groupTitle}").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.groupExpTxt.setOnClickListener {
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
            val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            val inputExp = inputEdt.text.toString()
            val alert = AlertDialog.Builder(mContext)
                .setMessage("모임의 목표를 입력해주세요")
                .setView(customView)
                .create()
            positiveBtn.text = "변경하기"
            inputEdt.hint = "목표를 입력해주세요"
            positiveBtn.setOnClickListener {
                database.getReference("data").child("goal").child("${GlobalData.groupTitle}").setValue(inputExp)
                Toast.makeText(mContext, "목표가 변경되었습니다", Toast.LENGTH_SHORT).show()
                alert.dismiss()
            }
            negativeBtn.setOnClickListener {
                alert.dismiss()
            }
            alert.show()
        }

    }

    override fun setValues() {
        binding.groupTitleTxt.text = GlobalData.groupTitle
        mMonthAdapter = MonthRecyclerAdapter(mContext, mMonthList)
        binding.monthRecyclerView.adapter = mMonthAdapter
        binding.monthRecyclerView.layoutManager = LinearLayoutManager(mContext)
        getAppointmentFromServer(now.time)
    }

    fun getAppointmentFromServer(date : Date){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestAppointment(token).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mMonthList.clear()

                    val br = response.body()!!
                    val appointmentList = br.data.appointments

                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val sdf = SimpleDateFormat("yyyy-MM")

                    for (appointment in appointmentList) {
                        val dateTime = formatter.parse(appointment.datetime)
                        if (
                            appointment.place == GlobalData.groupTitle
                            && sdf.format(dateTime) == sdf.format(date)
                        ) {
                            mMonthList.add(appointment)
                        }
                    }

                    mMonthAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}