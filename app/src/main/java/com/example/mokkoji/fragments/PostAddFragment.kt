package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentPostAddBinding
import com.example.mokkoji.databinding.FragmentPostBinding
import com.example.mokkoji.utils.ContextUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostAddFragment : BaseFragment() {

    lateinit var binding: FragmentPostAddBinding
    val database = FirebaseDatabase.getInstance("https://mokkoji-4e1ac-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_add, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.sendBtn.setOnClickListener {
            val inputTitle = binding.postTitleEdt.text.toString()
            val inputContent = binding.postContentEdt.text.toString()
            val deviceToken = ContextUtil.getDeviceToken(mContext)

            val inputMap = HashMap<String, String>()

            inputMap["title"] = inputTitle
            inputMap["content"] = inputContent
            inputMap["deviceToken"] = deviceToken

            database.getReference("post").setValue(inputMap)

        }
    }

    override fun setValues() {

    }
}