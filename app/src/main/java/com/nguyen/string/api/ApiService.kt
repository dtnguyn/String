package com.nguyen.string.api

import com.nguyen.string.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("users-register-email")
    fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("date_of_birth") dob: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): Call<ApiResponse<AuthData>>

    @FormUrlEncoded
    @POST("users-login")
    fun login(
        @Field("username") name: String,
        @Field("password") password: String,
        @Field("fcm_token") token: String
    ): Call<ApiResponse<AuthData>>

    @FormUrlEncoded
    @POST("users-register-facebook")
    fun registerWithFb(
        @Field("fb_token") fbToken: String?,
        @Field("fcm_token") fcmToken: String
    ): Call<ApiResponse<AuthData>>

    @FormUrlEncoded
    @POST("follow-users")
    fun followUser(
        @Field("users_id") userId: String,
        @Header("Authorization") token: String
    ): Call<ApiResponse<User>>


    @GET("interest-categories-list?")
    fun getInterestList(
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
        ): Call<ApiResponse<List<Interest>>>

    @GET("users-list?")
    fun getUserList(
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<User>>>

    @GET("feed")
    fun getFeed(
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Blog>>>


    @PUT("users-interest-categories-select")
    fun submitSelectedInterest(
        @Query("lists_interest[]") selectedInterestList: List<Interest>,
        @Header("Authorization") token: String
    ): Call<ApiResponse<User>>


}