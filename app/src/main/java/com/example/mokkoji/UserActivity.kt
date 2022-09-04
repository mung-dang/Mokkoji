package com.example.mokkoji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mokkoji.adapters.UserRecyclerAdapter
import com.example.mokkoji.databinding.ActivityUserBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.datas.UserData
import com.example.mokkoji.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : BaseActivity() {
    lateinit var binding : ActivityUserBinding
    lateinit var mUserAdapter : UserRecyclerAdapter
    val mUserList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.searchBtn.setOnClickListener {
            val searchNick = binding.userNickEdt.text.toString()
            val token = ContextUtil.getLoginToken(mContext)

            apiList.getSearchUser(token, searchNick).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        mUserList.clear()

                        val br = response.body()!!

                        mUserList.addAll(br.data.users)
                        mUserAdapter.notifyDataSetChanged()
                    }

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }
    }

    override fun setValues() {
        mUserAdapter = UserRecyclerAdapter(mContext, mUserList)
        binding.userRecyclerView.adapter = mUserAdapter
        binding.userRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
}