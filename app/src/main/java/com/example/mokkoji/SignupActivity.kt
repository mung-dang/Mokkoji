package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivitySignupBinding
import com.example.mokkoji.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : BaseActivity() {

    lateinit var binding: ActivitySignupBinding

    var isEmailOk = false
    var isNickOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.emailCheckBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            dupCheck("EMAIL", inputEmail)
        }
        binding.nickCheckBtn.setOnClickListener {
            val inputNick = binding.nickEdt.text.toString()
            dupCheck("NICK_NAME", inputNick)
        }
    }

    override fun setValues() {

    }

    fun dupCheck(type: String, value : String){
        apiList.getRequestCheck(type, value).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    when(type) {
                        "EMAIL" -> {
                            isEmailOk = true
                        }
                        "NICK_NAME" -> {
                            isNickOk = true
                        }
                    }
                }else{
                    val message = response.message()
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}