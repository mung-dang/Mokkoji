package com.example.mokkoji

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mokkoji.api.APIList
import com.example.mokkoji.api.ServerAPI
import retrofit2.Retrofit
import retrofit2.create

abstract class BaseActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var retrofit: Retrofit
    lateinit var apiList: APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        retrofit = ServerAPI.getRetrofit()
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()
}