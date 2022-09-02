package com.example.mokkoji.datas

data class DataResponse(
    val token : String,
    val user : UserData,
    val places : List<PlacesData>,
    val place : PlacesData,
    val appointment : AppointmentData,
    val appointments : List<AppointmentData>
){

}
