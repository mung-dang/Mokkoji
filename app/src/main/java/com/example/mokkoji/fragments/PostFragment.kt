package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.PostAddActivity
import com.example.mokkoji.R
import com.example.mokkoji.adapters.PostRecyclerAdapter
import com.example.mokkoji.databinding.FragmentPostBinding
import com.example.mokkoji.datas.PostData
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostFragment : BaseFragment() {

    lateinit var binding: FragmentPostBinding
    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")

    lateinit var mPostRecyclerAdapter: PostRecyclerAdapter
    val mPostList = ArrayList<PostData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.writeBtn.setOnClickListener {
            val myIntent = Intent(mContext, PostAddActivity::class.java)
                .putExtra("title", GlobalData.groupTitle)
            startActivity(myIntent)
        }

        database.getReference("data").child("post")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mPostList.add(0,
                        PostData(
                            snapshot.child("title").value.toString(),
                            snapshot.child("content").value.toString(),
                            snapshot.child("place").value.toString(),
                            snapshot.child("date").value.toString(),
                            snapshot.child("deviceToken").value.toString()
                        )
                    )
                    mPostRecyclerAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    override fun setValues() {
        mPostRecyclerAdapter = PostRecyclerAdapter(mContext, mPostList)
        binding.postRecyclerView.adapter = mPostRecyclerAdapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}