package com.example.mokkoji.datas

data class DataResponse(
    val token : String,
    val user : UserData,
    val places : List<PlacesData>
){

}
