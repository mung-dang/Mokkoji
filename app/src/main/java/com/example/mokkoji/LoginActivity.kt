package com.example.mokkoji

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivityLoginBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.android.material.internal.ContextUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.loginBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()
            apiList.postRequestLogin(inputEmail, inputPw).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful){
                        val br = response.body()!!

                        Toast.makeText(mContext, "${br.data.user.nick_name}님 환영합니다.", Toast.LENGTH_SHORT).show()

                        GlobalData.loginUser = br.data.user

                        ContextUtil.setAutoLogin(mContext, binding.autoLoginCb.isChecked)
                        ContextUtil.setLoginToken(mContext, br.data.token)

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()
                    }


                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }
        binding.signupBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignupActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {

    }
}