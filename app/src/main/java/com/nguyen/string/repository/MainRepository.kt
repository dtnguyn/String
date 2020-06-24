package com.nguyen.string.repository

import android.util.Log
import com.nguyen.string.MainApplication
import com.nguyen.string.api.MyRetrofitBuilder
import com.nguyen.string.data.*
import com.nguyen.string.util.SavedSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository{

    private const val ITEM_PER_PAGE = 10

    var verificationEmail : String? = null
    private var currentLoggedUser: AuthData? = null
    private var feedCurrentPage: Int = 1
    private var isLoadingMoreFeed = false


    init {
        SavedSharedPreferences.loggedUser.let {
            currentLoggedUser = it
            Log.d("Login", "token: ${it?.accessToken}")
        }
    }

    fun registerUser(name: String, username: String, dob: String, email: String, password: String, confirmPassword: String, callback: (ApiResponse<AuthData>) -> Unit){
        val call = MyRetrofitBuilder.apiService.register(name, username, dob, email, password, confirmPassword)
        call.enqueue(object: Callback<ApiResponse<AuthData>>{
            override fun onFailure(call: Call<ApiResponse<AuthData>>, t: Throwable) {
                Log.d("Register", "Fail to register ${t.message}")

            }

            override fun onResponse(call: Call<ApiResponse<AuthData>>, response: Response<ApiResponse<AuthData>>) {
                Log.d("Register", "Register successfully: ${response.body()?.data?.email}")
                response.body().let {
                    verificationEmail = it?.data?.email
                    callback.invoke(it!!)
                }
            }
        })
    }

    fun loginUser(email: String, password: String, callback: (ApiResponse<AuthData>) -> Unit){
        val call =
            MainApplication.token?.let {
                MyRetrofitBuilder.apiService.login(email, password, it)
            }



        call?.enqueue(object : Callback<ApiResponse<AuthData>>{
            override fun onFailure(call: Call<ApiResponse<AuthData>>, t: Throwable) {
                Log.d("Login", "Fail to log in ${t.message}")
            }

            override fun onResponse(call: Call<ApiResponse<AuthData>>, response: Response<ApiResponse<AuthData>>) {

                response.body().let {
                    if(it?.status!!){
                        Log.d("Login", "Login successfully: ${it.message}")
                        currentLoggedUser = it.data
                        SavedSharedPreferences.isLogin = true
                        SavedSharedPreferences.loggedUser = it.data
                        callback.invoke(it)
                    } else Log.d("Login", "Fail to log in ${it.message}")

                }
            }

        })
    }

    fun registerWithFb(fbToken: String?, callback: (ApiResponse<AuthData>) -> Unit){
        val call = MainApplication.token?.let { MyRetrofitBuilder.apiService.registerWithFb(fbToken, it) }

        call?.enqueue(object  : Callback<ApiResponse<AuthData>>{
            override fun onFailure(call: Call<ApiResponse<AuthData>>, t: Throwable) {
                Log.d("Register fb", "Fail to register ${t.message}")
            }

            override fun onResponse(call: Call<ApiResponse<AuthData>>, response: Response<ApiResponse<AuthData>>) {
                Log.d("Register fb", "Register successfully: ${response.body()}")
//                callback.invoke(response.body()!!)
            }

        })
    }

    fun getInterestList(page: String? = null, currentPage: String? = null, callback: (ApiResponse<List<Interest>>?) -> Unit){

        val call = currentLoggedUser?.accessToken?.let {
            MyRetrofitBuilder.apiService.getInterestList(page, currentPage, "Bearer $it")
        }
        call?.enqueue(object : Callback<ApiResponse<List<Interest>>>{
            override fun onFailure(call: Call<ApiResponse<List<Interest>>>, t: Throwable) {
                Log.d("Interest", "Fail to get interest list ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Interest>>>,
                response: Response<ApiResponse<List<Interest>>>
            ) {
                Log.d("Interest", "Success ${response.body()}")
                callback.invoke(response.body())
            }

        })
    }

    fun getUserList(page: String? = null, currentPage: String? = null, callback: (ApiResponse<List<User>>?) -> Unit){
        val call = currentLoggedUser?.accessToken?.let {
            MyRetrofitBuilder.apiService.getUserList(page, currentPage, "Bearer $it")
        }
        call?.enqueue(object : Callback<ApiResponse<List<User>>>{
            override fun onFailure(call: Call<ApiResponse<List<User>>>, t: Throwable) {
                Log.d("User", "Fail to get user list ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<List<User>>>,
                response: Response<ApiResponse<List<User>>>
            ) {
                Log.d("User", "Success ${response.body()}")
                callback.invoke(response.body())
            }

        })
    }


    fun followUser(userId: String){
        val call = currentLoggedUser?.accessToken?.let{
            Log.d("User", "id: $userId")
            Log.d("User", "token: $it")
            MyRetrofitBuilder.apiService.followUser(userId, "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<User>>{
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                Log.d("User", "Fail to follow user ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                Log.d("User", "Success to follow user ${response.body()}")
            }

        })
    }

    fun submitSelectedInterestList(interests: List<Interest>){
        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.submitSelectedInterest(interests, "Bearer $it")
        }

        call?.enqueue(object: Callback<ApiResponse<User>>{
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                Log.d("Interest", "Fail to submit interest list ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                Log.d("Interest", "success submit interest list ${response.body()}")
            }

        })
    }

    fun getFeed(callback: (ApiResponse<List<Blog>>?) -> Unit){
        feedCurrentPage = 1 //Initialize it back to the first page
        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.getFeed(feedCurrentPage.toString(), ITEM_PER_PAGE.toString(), "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Feed", "Fail to load feed ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                response.body().let {
                    Log.d("Feed", "Success: ${it?.message}")
                    callback(it)
                }
            }
        })
    }

    fun getMoreFeed(callback: (ApiResponse<List<Blog>>?) -> Unit){
        if(isLoadingMoreFeed) return
        Log.d("Feed", "Loading more feed")
        feedCurrentPage++
        isLoadingMoreFeed = true
        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.getFeed(feedCurrentPage.toString(), ITEM_PER_PAGE.toString(), "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Feed", "Fail to load feed ${t.message}")
                isLoadingMoreFeed = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                isLoadingMoreFeed = false
                response.body().let {
                    Log.d("Feed", "Success: ${it?.message}")
                    callback(it)
                }
            }
        })
    }


}