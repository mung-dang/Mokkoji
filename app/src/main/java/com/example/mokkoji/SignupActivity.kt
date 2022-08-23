package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.databinding.ActivitySignupBinding
import com.example.mokkoji.datas.BasicResponse
import org.json.JSONObject
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
        binding.signupBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            val inputNick = binding.nickEdt.text.toString()
            val inputPassword = binding.passwordEdt.toString()
            val passwordCheck = binding.passwordCheckEdt.toString()

            if (inputEmail.isBlank()){
                Toast.makeText(mContext, "이메일을 입력하지 않으셨어요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(inputPassword.isBlank()){
                Toast.makeText(mContext, "비밀번호를 입력하지 않으셨어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(inputPassword != passwordCheck){
                Toast.makeText(mContext, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(inputNick.isBlank()){
                Toast.makeText(mContext, "닉네임을 입력하지 않으셨어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isEmailOk || !isNickOk){
                Toast.makeText(mContext, "중복확인을 하지 않으셨어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiList.putRequestSingUp(
                inputEmail, inputPassword, inputNick
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        Toast.makeText(mContext, "가입을 환영합니다!!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else{
                        val errorBody = response.errorBody()!!.string()
                        val jsonObj = JSONObject(errorBody)
                        val code = jsonObj.getInt("code")
                        val message = jsonObj.getString("message")
                        if(code == 400){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(mContext, "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

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