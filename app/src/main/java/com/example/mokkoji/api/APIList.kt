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

    @GET("/user")
    fun getRequestInfo(
        @Header("X-Http-Token") token : String
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun postRequestAddGroup(
        @Header("X-Http-Token") token: String,
        @Field("name") title: String,
        @Field("latitude") latitude : Int,
        @Field("longitude") longitude : Int,
        @Field("is_primary") primary : Boolean
    ) : Call<BasicResponse>

    @GET("/user/place")
    fun getRequestAddGroup(
        @Header("X-Http-Token") token: String
    ) : Call<BasicResponse>
}