package com.example.mokkoji.colosseumapi

import com.example.mokkoji.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface ColosseumAPIList {

    @FormUrlEncoded
    @POST("/topic_reply")
    fun postRequestReply(
        @Field("topic_id") topic_id : Int?,
        @Field("content") content : String,
        @Field("parent_reply_id") reply_id : Int?
    ): Call<BasicResponse>

}