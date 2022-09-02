package com.example.mokkoji.colosseumapi

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ColosseumServerAPI {

    companion object{

        private val BASE_URL = "http://54.180.52.26/"

        private var retrofit : Retrofit? = null

        private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6MjI1LCJlbWFpbCI6ImttY2ttY0B0ZXN0LmNvbSIsInBhc3N3b3JkIjoiNTk4NmFjY2QyYmNiMzkzNDllMmE5NzhmMTAzMGM4N2QifQ.XZUV5HXLLcSsvsRWzWr3KSIKLHs_5ZOEZQBDOH_KNoZmFnrZOO9nts-TZZ2jySqnvj0ETcQmb0YO1hy9CujZQA"

        fun getRetrofit() : Retrofit {
            if(retrofit == null){

                val interceptor = Interceptor{
                    with(it) {
                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", token)
                            .build()
                        proceed(newRequest)
                    }
                }

                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(myClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!
        }
    }
}