package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityAddGroupBinding
import com.example.mokkoji.databinding.ActivityGroupBinding

class AddGroupActivity : BaseActivity() {

    lateinit var binding: ActivityAddGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}