package com.example.mokkoji.utils

import com.example.mokkoji.datas.AppointmentData
import com.example.mokkoji.datas.PlacesData
import com.example.mokkoji.datas.UserData

class GlobalData {
    companion object{
        var loginUser : UserData? = null
        var groupPlace : PlacesData? = null
    }
}