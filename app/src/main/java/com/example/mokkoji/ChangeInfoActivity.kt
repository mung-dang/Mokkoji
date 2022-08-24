package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityAddGroupBinding
import com.example.mokkoji.databinding.ActivityChangeInfoBinding
import com.example.mokkoji.databinding.ActivityGroupBinding

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