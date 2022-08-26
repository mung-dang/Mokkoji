package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.AddGroupActivity
import com.example.mokkoji.R
import com.example.mokkoji.adapters.GroupRecyclerAdapter
import com.example.mokkoji.databinding.FragmentGroupBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.PlacesData
import com.example.mokkoji.utils.ContextUtil
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
            val myIntent = Intent(mContext, AddGroupActivity::class.java)
            startActivityForResult(myIntent, 1000)
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