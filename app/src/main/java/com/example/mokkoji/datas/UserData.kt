package com.example.mokkoji.datas

import com.google.gson.annotations.SerializedName

data class UserData(
    val id : Int,
    val email : String,
    val nick_name : String,
    @SerializedName("profile_img")
    val profileImg : String,
    val date : String
){

}
