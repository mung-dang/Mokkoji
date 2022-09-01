package com.example.mokkoji

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
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

    fun setCustomActionBar (){
        val defaultActionBar = supportActionBar!!
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.custom_action_bar)
        val myToolbar = defaultActionBar.customView.parent as androidx.appcompat.widget.Toolbar
        myToolbar.setContentInsetsAbsolute(0,0)
    }


}