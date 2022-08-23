package com.example.mokkoji.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.R
import com.example.mokkoji.adapters.GroupRecyclerAdapter
import com.example.mokkoji.databinding.FragmentGroupBinding
import com.example.mokkoji.datas.GroupData

class GroupFragment : BaseFragment() {

    lateinit var binding: FragmentGroupBinding

    lateinit var mGroupAdapter: GroupRecyclerAdapter
    val mGroupList = ArrayList<GroupData>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)
        return binding.root
    }
    override fun setupEvents() {

    }

    override fun setValues() {
        mGroupList.add(GroupData( "학원가기", "수학학원가는 모임"))
        mGroupList.add(GroupData( "배드민턴", "배드민턴치는 모임"))
        mGroupList.add(GroupData( "게임", "게임하는 모임"))

        mGroupAdapter = GroupRecyclerAdapter(mContext, mGroupList)
        binding.groupRecyclerView.adapter = mGroupAdapter
        binding.groupRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}