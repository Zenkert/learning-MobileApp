package com.example.chat_bot.networking.Retrofit.Seeds_api.api

import McqsList
import com.example.chat_bot.Lists.Quest
import com.example.chat_bot.Lists.*
import com.example.chat_bot.Room.Entities.OnlineUserData
import com.example.chat_bot.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SEEDSApi {

    @GET("topic")
   suspend fun getallTopics() : Response<TopicsList>

    @GET("true_false")
     fun getallTF() : Call<trufalses>

    @GET("subject/get")
     fun getallSubjects() : Call<SubjectList>

    @GET("mcqs")
     fun getMcqs() : Call<McqsList>

    @GET("topic/getByTopic/{topicID}")
    fun getQuiz(
        @Path("topicID" ) topicID: String
    ) : Call<Quest>

    @POST("openAnswer/create")
    suspend fun submitOpenEnded(@Body openEnded: com.example.chat_bot.data.OpenEnded): Response<openEndedResponse>

    @GET("grade/get")
     fun getGrades() : Call<gradeList>

    @POST("students/create")
    suspend fun create_user(@Body user: Userinfo): Response<LoginData>

    @GET("students/getUser/{name}")
    fun getUserbyUsername(
        @Path("name" ) name: String
    ) : Call<OnlineUserData>


    @GET("age/get")
     fun getAgegroups() : Call<AgegroupList>

    companion object {
        var retrofitService: SEEDSApi? = null

        fun getInstance(): SEEDSApi {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://ec2-3-71-216-21.eu-central-1.compute.amazonaws.com:5000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(SEEDSApi::class.java)
            }
            return retrofitService!!
        }


    }

}