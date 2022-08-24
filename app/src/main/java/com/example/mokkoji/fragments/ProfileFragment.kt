package com.example.mokkoji.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.ChangeInfoActivity
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentProfileBinding

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
            Toast.makeText(mContext, "변경하시겠습니까", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setValues() {

    }
}