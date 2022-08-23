package com.example.mokkoji

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.google.android.material.internal.ContextUtils

class SplashActivity : BaseActivity() {

    var isTokenOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed(
            {
                val isAutoLogin = ContextUtil.getAutoLogin(mContext)

                if(isTokenOk && isAutoLogin){
                    Toast.makeText(mContext, "환영합니다.", Toast.LENGTH_SHORT).show()
                    val myIntent = Intent(mContext, MainActivity::class.java)
                    startActivity(myIntent)
                }else{
                    val myIntent = Intent(mContext, LoginActivity::class.java)
                    startActivity(myIntent)
                }
                finish()
            }, 2000)
    }
}