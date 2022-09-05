package com.example.mokkoji

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityChangeInfoBinding
import com.example.mokkoji.databinding.ActivityPostAddBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostAddActivity : BaseActivity() {

    lateinit var binding: ActivityPostAddBinding
    val database = FirebaseDatabase.getInstance("https://realtimedb-441a2-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_add)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.sendBtn.setOnClickListener {
            val inputTitle = binding.postTitleEdt.text.toString()
            val inputContent = binding.postContentEdt.text.toString()
            val deviceToken = ContextUtil.getDeviceToken(mContext)
            val place = intent.getStringExtra("title").toString()
            val now = Calendar.getInstance()
            val sdf = SimpleDateFormat("M/d HH:mm")
            val nowString = sdf.format(now.time)

            val inputMap = HashMap<String, String>()

            inputMap["title"] = inputTitle
            inputMap["content"] = inputContent
            inputMap["place"] = place
            inputMap["date"] = nowString
            inputMap["deviceToken"] = deviceToken

            database.getReference("data").child("${place}").push().setValue(inputMap)

            finish()
        }
    }

    override fun setValues() {
        val defaultActionBar = supportActionBar!!
        defaultActionBar.hide()
    }
}