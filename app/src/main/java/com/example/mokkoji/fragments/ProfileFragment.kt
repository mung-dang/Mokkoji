package com.example.mokkoji.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.mokkoji.ChangeInfoActivity
import com.example.mokkoji.LoginActivity
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentProfileBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    var isNickOk = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.changePwBtn.setOnClickListener {
            val myIntent = Intent(mContext, ChangeInfoActivity::class.java)
            startActivityForResult(myIntent, 1000)
        }

        binding.nickTxt.setOnClickListener {
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)

            val alert = AlertDialog.Builder(mContext)
                .setTitle("닉네임 변경")
                .setView(customView)
                .setPositiveButton("변경하기", DialogInterface.OnClickListener { dialogInterface, i ->
                    val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
                    val inputNick = inputEdt.text.toString()
                    val token = ContextUtil.getLoginToken(mContext)

                    if (inputNick.isBlank()){
                        Toast.makeText(mContext, "입력되지 않았습니다", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }

                    apiList.getRequestCheck("NICK_NAME", inputNick).enqueue(object : Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                isNickOk = true
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                    })

                    if (!isNickOk){
                        Toast.makeText(mContext, "중복된 닉네임이 있습니다", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }

                    apiList.patchRequestNickChange(token, "nickname", inputNick).enqueue(object : Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if (response.isSuccessful){
                                Toast.makeText(mContext, "닉네임이 변경되었습니다", Toast.LENGTH_SHORT).show()
                                GlobalData.loginUser = response.body()!!.data.user

                                binding.nickTxt.text = GlobalData.loginUser!!.nick_name
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                    })

                })
                .setNegativeButton("취소", null)
                .show()
        }

        binding.logoutBtn.setOnClickListener {
            ContextUtil.clearData(mContext)

            val myIntent = Intent(mContext, LoginActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        val nick_name = GlobalData.loginUser!!.nick_name
        binding.nickTxt.text = nick_name

    }
}