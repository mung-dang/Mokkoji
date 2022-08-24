package com.example.mokkoji.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mokkoji.api.APIList
import com.example.mokkoji.api.ServerAPI
import retrofit2.Retrofit

abstract class BaseFragment : Fragment() {

    lateinit var mContext : Context
    lateinit var retrofit : Retrofit
    lateinit var apiList : APIList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()
        retrofit = ServerAPI.getRetrofit()
        apiList = retrofit.create(APIList::class.java)
    }

    abstract fun setupEvents()
    abstract fun setValues()

}