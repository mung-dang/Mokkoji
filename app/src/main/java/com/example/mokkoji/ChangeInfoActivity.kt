package com.example.mokkoji

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityChangeInfoBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeInfoActivity : BaseActivity() {

    lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_info)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.changePwBtn.setOnClickListener {
            val token = ContextUtil.getLoginToken(mContext)
            val currentPw = binding.currentPw.text.toString()
            val changePw = binding.changePw.text.toString()
            val changePwCheck = binding.changePwCheck.text.toString()

            if(currentPw.isBlank() || changePw.isBlank() || changePwCheck.isBlank()){
                Toast.makeText(mContext, "공백이 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (changePw != changePwCheck){
                Toast.makeText(mContext, "변경할 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.patchRequestPwChange(token, currentPw, changePw).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        var br = response.body()!!
                        Toast.makeText(mContext, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show()
                        finish()

                        ContextUtil.setLoginToken(mContext, response.body()!!.data.token)

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