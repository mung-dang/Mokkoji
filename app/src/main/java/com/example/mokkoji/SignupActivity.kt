package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            val inputPassword = binding.passwordEdt.text.toString()
            val passwordCheck = binding.passwordCheckEdt.text.toString()
            val inputNick = binding.nickEdt.text.toString()

            if (inputEmail.isBlank() || inputPassword.isBlank() || inputNick.isBlank()){
                Toast.makeText(mContext, "공백이 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(inputPassword.length < 8){
                Toast.makeText(mContext, "비밀번호는 8자 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(inputPassword != passwordCheck){
                Toast.makeText(mContext, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(inputNick.length < 3){
                Toast.makeText(mContext, "닉네임은 3자 이상이어야 합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isEmailOk || !isNickOk){
                Toast.makeText(mContext, "중복확인을 하지 않으셨습니다", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(mContext, "가입을 환영합니다!", Toast.LENGTH_SHORT).show()
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
        setCustomActionBar()
    }

    fun dupCheck(type: String, value : String){
        apiList.getRequestCheck(type, value).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    when(type) {
                        "EMAIL" -> isEmailOk = true
                        "NICK_NAME" -> isNickOk = true
                    }
                    Toast.makeText(mContext, "중복된 정보가 없습니다", Toast.LENGTH_SHORT).show()
                }else{
                    val errorBody = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBody)
                    val code = jsonObj.getInt("code")
                    val message = jsonObj.getString("message")
                    if(code == 400){
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}