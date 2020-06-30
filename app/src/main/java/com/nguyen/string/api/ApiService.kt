package com.nguyen.string.api

import com.nguyen.string.data.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
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

    @FormUrlEncoded
    @POST("post-save")
    fun save(
        @Field("id") id: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<Blog>>

    @FormUrlEncoded
    @POST("comment-add")
    fun addComment(
        @Field("ipps_id") id: Int,
        @Field("comment") comment: String?,
        @Field("replyID") replyId: String?,
        @Field("commentchildID") commentChildId: String?,
        @Field("tagUsername[]") tags: List<String>?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<Comment>>



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

    @GET("comments-list/{ipps_id}")
    fun getComments(
        @Path("ipps_id") id: Int?,
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Comment>>>

    @GET("profile/{users_id}")
    fun getUserProfile(
        @Path("users_id") userId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<User>>

    @GET("profile-post/{user_id}")
    fun getUserProfilePosts(
        @Path("user_id") userId: Int,
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Blog>>>

    @GET("post-detail/{id_post}")
    fun getPostDetail(
        @Path("id_post") postId: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<Blog>>

    @GET("profile-itinerary/{user_id}")
    fun getUserProfileItinerary(
        @Path("user_id") userId: Int,
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Blog>>>

    @GET("profile-save/{user_id}")
    fun getUserProfileSavedPosts(
        @Path("user_id") userId: Int,
        @Query("page") page: String?,
        @Query("current_per_page") currentPage: String?,
        @Header("Authorization") token: String
    ): Call<ApiResponse<List<Blog>>>


    @PUT("users-interest-categories-select")
    fun submitSelectedInterest(
        @Query("lists_interest[]") selectedInterestList: List<Interest>,
        @Header("Authorization") token: String
    ): Call<ApiResponse<User>>


    @FormUrlEncoded
    @PUT("like")
    fun like(
        @Field("ipps_id")id: Int,
        @Header("Authorization") token: String
    ): Call<ApiResponse<Blog>>




}