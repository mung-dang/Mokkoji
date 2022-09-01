package com.example.mokkoji.datas

import com.google.gson.annotations.SerializedName

data class AppointmentData(
    val id : Int,
    val title : String,
    val date : String,
    val datetime : String,
    val place : String
){

}
