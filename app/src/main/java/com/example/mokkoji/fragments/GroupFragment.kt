package com.example.mokkoji.fragments

import android.app.AlertDialog
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
import com.example.mokkoji.adapters.GroupRecyclerAdapter
import com.example.mokkoji.databinding.FragmentGroupBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.PlacesData
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupFragment : BaseFragment() {

    lateinit var binding: FragmentGroupBinding
    lateinit var mGroupAdapter: GroupRecyclerAdapter
    val mGroupList = ArrayList<PlacesData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.addGroupBtn.setOnClickListener {
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
            val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)

            val alert = AlertDialog.Builder(mContext)
                .setMessage("새로운 모꼬지 추가하기")
                .setView(customView)
                .create()
            positiveBtn.text = "추가하기"
            inputEdt.hint = "모꼬지 이름 입력하기"
            positiveBtn.setOnClickListener {

                val inputTitle = inputEdt.text.toString()
                val token = ContextUtil.getLoginToken(mContext)

                if(inputTitle.isBlank()){
                    Toast.makeText(mContext, "공백없이 채워주세요", Toast.LENGTH_SHORT).show()
                }

                apiList.postRequestAddGroup(token, inputTitle, 0, 0, false).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            val br = response.body()!!
                            colosseumApiList.postRequestReply(4, br.data.place.title, null).enqueue(object : Callback<BasicResponse>{
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {

                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }
                            })

                            Toast.makeText(mContext, "새 모꼬지가 추가되었습니다", Toast.LENGTH_SHORT).show()
                            getGroupDataFromServer()
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

    override fun setValues() {
        mGroupAdapter = GroupRecyclerAdapter(mContext, mGroupList)
        binding.groupRecyclerView.adapter = mGroupAdapter
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(mContext)
        getGroupDataFromServer()
    }

    fun getGroupDataFromServer(){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestAddGroup(token).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mGroupList.clear()

                    val br = response.body()!!

                    mGroupList.addAll(br.data.places)
                    mGroupAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}