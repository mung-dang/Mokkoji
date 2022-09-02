package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentPostBinding
import com.google.firebase.database.FirebaseDatabase

class PostFragment : BaseFragment() {

    lateinit var binding: FragmentPostBinding
    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")

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
            val myIntent = Intent(mContext, PostAddFragment::class.java)
            startActivityForResult(myIntent, 1001)
        }
    }

    override fun setValues() {

    }
}