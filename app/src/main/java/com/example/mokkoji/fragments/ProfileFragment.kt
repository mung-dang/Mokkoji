package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.ChangeInfoActivity
import com.example.mokkoji.LoginActivity
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentProfileBinding
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.profileLayout.setOnClickListener {
            val myIntent = Intent(mContext, ChangeInfoActivity::class.java)
            startActivityForResult(myIntent, 1000)
        }
        binding.logoutBtn.setOnClickListener {
            ContextUtil.clearData(mContext)

            val myIntent = Intent(mContext, LoginActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        val nick_name = GlobalData.loginUser!!.nick_name
        binding.nickTxt.text = nick_name

    }
}