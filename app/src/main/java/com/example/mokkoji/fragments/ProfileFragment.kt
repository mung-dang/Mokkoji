package com.example.mokkoji.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mokkoji.ChangeInfoActivity
import com.example.mokkoji.LoginActivity
import com.example.mokkoji.R
import com.example.mokkoji.databinding.FragmentProfileBinding
import com.example.mokkoji.datas.BasicResponse
import com.example.mokkoji.utils.ContextUtil
import com.example.mokkoji.utils.GlobalData
import com.example.mokkoji.utils.URIPathHelper
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileFragment : BaseFragment() {

    lateinit var binding: FragmentProfileBinding
    var isNickOk = false
    private  val REQ_FOR_GALLERY = 1000

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

        binding.profileImg.setOnClickListener {
            val pl = object : PermissionListener{
                override fun onPermissionGranted() {
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startActivityForResult(myIntent, REQ_FOR_GALLERY)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "권한이 없습니다", Toast.LENGTH_SHORT).show()
                }

            }

            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
        }

        binding.loginInfoTxtLayout.setOnClickListener {
            if(binding.loginInfoLayout.visibility == View.GONE){
                binding.loginInfoLayout.visibility = View.VISIBLE
            }else{
                binding.loginInfoLayout.visibility = View.GONE
            }
        }

        binding.changePwBtn.setOnClickListener {
            val myIntent = Intent(mContext, ChangeInfoActivity::class.java)
            startActivityForResult(myIntent, 1000)
        }

        binding.nickTxt.setOnClickListener {
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            val positiveBtn = customView.findViewById<Button>(R.id.positiveBtn)
            val negativeBtn = customView.findViewById<Button>(R.id.negativeBtn)


            val alert = AlertDialog.Builder(mContext)
                .setMessage("닉네임을 변경하시겠습니까?")
                .setView(customView)
                .create()

                positiveBtn.setOnClickListener {
                    val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
                    val inputNick = inputEdt.text.toString()
                    val token = ContextUtil.getLoginToken(mContext)

                    if (inputNick.isBlank()){
                        Toast.makeText(mContext, "입력되지 않았습니다", Toast.LENGTH_SHORT).show()
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
                    if (inputNick.isBlank()){
                        Toast.makeText(mContext, "변경할 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (!isNickOk){
                        Toast.makeText(mContext, "중복된 닉네임이 있습니다", Toast.LENGTH_SHORT).show()
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
                                alert.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }
                    })
                }

            negativeBtn.setOnClickListener {
                alert.dismiss()
            }
            alert.show()
        }

        binding.logoutBtn.setOnClickListener {
            ContextUtil.clearData(mContext)

            GlobalData.loginUser = null

            val myIntent = Intent(mContext, LoginActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun setValues() {
        Glide.with(mContext)
            .load(GlobalData.loginUser!!.profileImg)
            .into(binding.profileImg)

        val nick_name = GlobalData.loginUser!!.nick_name
        binding.nickTxt.text = nick_name

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQ_FOR_GALLERY){
                val selectedImgUri = data?.data!!
                val file = File(URIPathHelper().getPath(mContext, selectedImgUri))
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)
                val multiPartBody = MultipartBody.Part.createFormData("profile_image", "myProfile.jpg", fileReqBody)

                val token = ContextUtil.getLoginToken(mContext)
                apiList.putRequestUserImage(token, multiPartBody).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(mContext, "프로필 사진이 변경되었습니다", Toast.LENGTH_SHORT).show()
                            GlobalData.loginUser = response.body()!!.data.user
                            Glide.with(mContext)
                                .load(GlobalData.loginUser!!.profileImg)
                                .into(binding.profileImg)
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })
            }
        }

    }
}