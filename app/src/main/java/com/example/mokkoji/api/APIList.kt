package com.example.mokkoji.api

import com.example.mokkoji.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSingUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nick_name") nick: String
    ) : Call<BasicResponse>

    @GET("/user/check")
    fun getRequestCheck(
        @Query("type") type : String,
        @Query("value") value : String
    ) : Call<BasicResponse>
}