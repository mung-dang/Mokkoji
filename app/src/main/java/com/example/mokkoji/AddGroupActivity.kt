package com.example.mokkoji

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.adapters.GroupRecyclerAdapter
import com.example.mokkoji.databinding.ActivityAddGroupBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.PlacesData
import com.example.mokkoji.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupActivity : BaseActivity() {

    lateinit var binding: ActivityAddGroupBinding
    lateinit var mGroupAdapter: GroupRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.addGroupBtn.setOnClickListener {
            val inputTitle = binding.titleEdt.text.toString()
            val inputGoal = binding.goalEdt.text
            val token = ContextUtil.getLoginToken(mContext)

            if(inputTitle.isBlank() || inputGoal.isBlank()){
                Toast.makeText(mContext, "공백없이 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.postRequestAddGroup(token, inputTitle, 0, 0, false).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        mGroupAdapter.notifyDataSetChanged()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })


        }
    }

    override fun setValues() {
        setCustomActionBar()
    }
}