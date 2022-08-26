package com.example.mokkoji

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityChangeInfoBinding

class ChangeInfoActivity : BaseActivity() {

    lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_info)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


    }

    override fun setValues() {

    }
}