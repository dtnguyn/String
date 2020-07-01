package com.nguyen.string.repository

import android.util.Log
import androidx.room.Room
import com.nguyen.string.MainApplication
import com.nguyen.string.api.MyRetrofitBuilder
import com.nguyen.string.data.*
import com.nguyen.string.data.database.*
import com.nguyen.string.util.SavedSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object MainRepository{

    private const val ITEM_PER_PAGE = 10

    var verificationEmail : String? = null
    private var currentLoggedUser: AuthData? = null
    private var currentUserProfile: Profile? = null
    private var currentUserProfilePosts: ProfilePosts? = null
    private var currentUserProfileItineraries: ProfileItineraries? = null
    private var currentUserProfileSavedPosts: ProfileSavedPosts? = null

    private var userFollowCurrentPage: Int = 1
    private var isLoadingUserFollow = false

    private var feedCurrentPage: Int = 1
    private var isLoadingMoreFeed = false

    private var commentCurrentPage: Int = 1
    private var isLoadingMoreComments = false

    private var profilePostsCurrentPage: Int = 1
    private var isLoadingProfilePosts = false

    private var profileItinerariesCurrentPage: Int = 1
    private var isLoadingProfileItineraries = false

    private var profileSavedPostsCurrentPage: Int = 1
    private var isLoadingProfileSavedPosts = false

    private var profileDao: ProfileDao? = null

    init {

        val db = MainApplication.context?.let { ProfileDatabase.getDatabase(it) }
        profileDao = db?.profileDao()


        SavedSharedPreferences.loggedUser?.let {
            currentLoggedUser = it

            CoroutineScope(Dispatchers.IO).launch {
                currentUserProfile = profileDao?.getProfile(it.id!!)
                currentUserProfilePosts = profileDao?.getProfilePosts(it.id!!)
                currentUserProfileItineraries = profileDao?.getProfileItineraries(it.id!!)
                currentUserProfileSavedPosts = profileDao?.getProfileSavedPosts(it.id!!)
            }

            Log.d("Login", "token: ${it.accessToken}")
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
                    } else Log.d("Login", "Fail to log in ${it.message}")
                    callback.invoke(it)
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

    fun getUserList(callback: (ApiResponse<List<User>>?) -> Unit){
        userFollowCurrentPage = 1
        val call = currentLoggedUser?.accessToken?.let {
            MyRetrofitBuilder.apiService.getUserList(userFollowCurrentPage.toString(), ITEM_PER_PAGE.toString(), "Bearer $it")
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

    fun getMoreUserList(callback: (ApiResponse<List<User>>?) -> Unit){
        if(isLoadingUserFollow) return
        userFollowCurrentPage++
        isLoadingUserFollow = true
        val call = currentLoggedUser?.accessToken?.let {
            MyRetrofitBuilder.apiService.getUserList(userFollowCurrentPage.toString(), ITEM_PER_PAGE.toString(), "Bearer $it")
        }
        call?.enqueue(object : Callback<ApiResponse<List<User>>>{
            override fun onFailure(call: Call<ApiResponse<List<User>>>, t: Throwable) {
                Log.d("User", "Fail to get user list ${t.message}")
                isLoadingUserFollow = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<User>>>,
                response: Response<ApiResponse<List<User>>>
            ) {
                isLoadingUserFollow = false
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
            Log.d("Feed", "Getting feed...")
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
                if(response.body() == null){
                    Log.d("Feed", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, ArrayList<Blog>()))
                } else {
                    Log.d("Feed", "Success: ${response.body()?.message}")
                    callback.invoke(response.body())
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
                    callback.invoke(it)
                }
            }
        })
    }

    fun like(id: Int){

        Log.d("Like", "id: $id")

        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.like(id, "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<Blog>>{
            override fun onFailure(call: Call<ApiResponse<Blog>>, t: Throwable) {
                Log.d("Like", "Fail: ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<Blog>>,
                response: Response<ApiResponse<Blog>>
            ) {

                response.body()?.let {
                    Log.d("Like", "Success: ${response.body()?.message}")
                }
            }

        })
    }


    fun save(id: Int){

        Log.d("Save", "id: $id")

        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.save(id, "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<Blog>>{
            override fun onFailure(call: Call<ApiResponse<Blog>>, t: Throwable) {
                Log.d("Save", "Fail: ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<Blog>>,
                response: Response<ApiResponse<Blog>>
            ) {

                response.body()?.let {
                    Log.d("Save", "Success: ${response.body()?.message}")
                }
            }

        })
    }

    fun getComments(id: Int, callback: (ApiResponse<List<Comment>>) -> Unit){

        commentCurrentPage = 1
        val call = currentLoggedUser?.accessToken?.let{
            Log.d("Comment", "Getting comments... $id $it")
            MyRetrofitBuilder.apiService.getComments(id, commentCurrentPage.toString(), ITEM_PER_PAGE.toString(), "Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<List<Comment>>>{
            override fun onFailure(call: Call<ApiResponse<List<Comment>>>, t: Throwable) {
                Log.d("Comment", "Fail: ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Comment>>>,
                response: Response<ApiResponse<List<Comment>>>
            ) {
                Log.d("Comment", "Success: ${response.body()}")
                response.body()?.let(callback)
            }
        })
    }

    fun getMoreComments(id: Int, callback: (ApiResponse<List<Comment>>) -> Unit){
        if(isLoadingMoreComments) return
        isLoadingMoreComments = true
        commentCurrentPage++

        Log.d("Comment", "Getting more comments... page $commentCurrentPage")
        val call = currentLoggedUser?.accessToken?.let{
            MyRetrofitBuilder.apiService.getComments(id, commentCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<List<Comment>>>{
            override fun onFailure(call: Call<ApiResponse<List<Comment>>>, t: Throwable) {
                Log.d("Comment", "Fail: ${t.message}")
                isLoadingMoreComments = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Comment>>>,
                response: Response<ApiResponse<List<Comment>>>
            ) {
                Log.d("Comment", "Success: ${response.body()?.message}")
                response.body()?.let(callback)
                isLoadingMoreComments = false
            }
        })
    }


    fun addComment(id: Int, comment: String, callback: (Boolean) -> Unit){

        val call = currentLoggedUser?.accessToken?.let{
            Log.d("Comment", "Adding comment... $id $it")
            MyRetrofitBuilder.apiService.addComment(id, comment, "", "", emptyList(),"Bearer $it")
        }

        call?.enqueue(object : Callback<ApiResponse<Comment>>{
            override fun onFailure(call: Call<ApiResponse<Comment>>, t: Throwable) {
                Log.d("Comment", "Fail: ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<Comment>>,
                response: Response<ApiResponse<Comment>>
            ) {
                Log.d("Comment", "Success: ${response.body()}")

                response.body()?.status?.let {
                    callback(it)
                }
            }
        })
    }

    fun getUserProfile(callback: (ApiResponse<User>) -> Unit){
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting user profile... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfile(it.id!!,"Bearer ${it.accessToken}")
        }

        call?.enqueue(object : Callback<ApiResponse<User>>{
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                currentUserProfile?.let {
                    callback(ApiResponse(null, "", true,
                        User(
                            id = it.id,
                            username = it.username,
                            bio = it.bio,
                            profilePhoto = it.profilePhoto,
                            postsCounter = it.postCount,
                            followerCounter = it.followerCount,
                            followingCounter = it.followingCount,
                            itineraryCounter = it.itineraryCount
                        )
                    ))
                }

            }

            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                Log.d("Profile", "Success: ${response.body()}")

                response.body()?.let {
                    callback(it)
                }

                response.body()?.data?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        profileDao?.setProfile(Profile(it.id, it.username, it.bio, it.profilePhoto, it.postsCounter, it.itineraryCounter, it.followingCounter, it.followerCounter))
                    }

                }
            }

        })
    }

    fun getUserProfilePosts(callback: (ApiResponse<List<Blog>>) -> Unit){
        profilePostsCurrentPage = 1
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting posts... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfilePosts(it.id!!, profilePostsCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object  : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                currentUserProfilePosts?.posts?.let{
                    callback.invoke(ApiResponse(400, "", false, it))
                }

            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, currentUserProfilePosts?.posts))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message}")
                    callback.invoke(response.body()!!)
                    response.body()?.data?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            profileDao?.setProfilePosts(ProfilePosts(currentLoggedUser?.id, it))
                        }
                    }
                }
            }
        })
    }

    fun getMoreUserProfilePosts(callback: (ApiResponse<List<Blog>>) -> Unit){
        if(isLoadingProfilePosts) return
        isLoadingProfilePosts = true
        profilePostsCurrentPage++
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting posts... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfilePosts(it.id!!, profilePostsCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object  : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                isLoadingProfilePosts = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, null))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message}")
                    callback.invoke(response.body()!!)
                }
                isLoadingProfilePosts = false
            }
        })
    }

    fun getPostDetail(postId: Int, callback: (ApiResponse<Blog>) -> Unit){
        val call = currentLoggedUser?.accessToken?.let{
            Log.d("Profile", "Getting post detail... $postId $it")
            MyRetrofitBuilder.apiService.getPostDetail(postId,"Bearer $it")
        }

        call?.enqueue(object: Callback<ApiResponse<Blog>>{
            override fun onFailure(call: Call<ApiResponse<Blog>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
            }

            override fun onResponse(
                call: Call<ApiResponse<Blog>>,
                response: Response<ApiResponse<Blog>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, null))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message}")
                    callback.invoke(response.body()!!)
                }
            }

        })
    }

    fun getUserProfileItinerary(callback: (ApiResponse<List<Blog>>) -> Unit){
        profileItinerariesCurrentPage = 1
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting itineraries... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfileItinerary(it.id!!, profileItinerariesCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object  : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                currentUserProfileItineraries?.itineraries?.let{
                    callback.invoke(ApiResponse(400, "", false, it))
                }

            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, null))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message} ${response.body()?.data?.size}")
                    callback.invoke(response.body()!!)
                    response.body()?.data?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            profileDao?.setProfileItineraries(ProfileItineraries(currentLoggedUser?.id, it))
                        }

                    }
                }
            }
        })
    }

    fun getMoreUserProfileItinerary(callback: (ApiResponse<List<Blog>>) -> Unit){
        if(isLoadingProfileItineraries) return
        isLoadingProfileItineraries = true
        profileItinerariesCurrentPage++

        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting itineraries... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfileItinerary(it.id!!, profileItinerariesCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object  : Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                isLoadingProfileItineraries = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, null))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message} ${response.body()?.data?.size}")
                    callback.invoke(response.body()!!)
                }
                isLoadingProfileItineraries = false
            }
        })
    }

    fun getUserProfileSavedPosts(callback: (ApiResponse<List<Blog>>) -> Unit){
        profileSavedPostsCurrentPage = 1
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting saved posts... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfileSavedPosts(it.id!!, profileSavedPostsCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object: Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                currentUserProfileSavedPosts?.savedPosts?.let {
                    callback.invoke(ApiResponse(400, "", false, it))
                }

            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, currentUserProfileSavedPosts?.savedPosts))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message} ${response.body()?.data?.size}")
                    callback.invoke(response.body()!!)

                    response.body()?.data?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            profileDao?.setProfileSavedPosts(ProfileSavedPosts(currentLoggedUser?.id, it))
                        }
                    }
                }
            }
        })
    }

    fun getMoreUserProfileSavedPosts(callback: (ApiResponse<List<Blog>>) -> Unit){
        if(isLoadingProfileSavedPosts) return
        isLoadingProfileSavedPosts = true
        profileSavedPostsCurrentPage++
        val call = currentLoggedUser?.let{
            Log.d("Profile", "Getting more saved posts... ${it.id} ${it.accessToken}")
            MyRetrofitBuilder.apiService.getUserProfileSavedPosts(it.id!!, profileSavedPostsCurrentPage.toString(), ITEM_PER_PAGE.toString(),"Bearer ${it.accessToken}")
        }

        call?.enqueue(object: Callback<ApiResponse<List<Blog>>>{
            override fun onFailure(call: Call<ApiResponse<List<Blog>>>, t: Throwable) {
                Log.d("Profile", "Fail: ${t.message}")
                isLoadingProfileSavedPosts = false
            }

            override fun onResponse(
                call: Call<ApiResponse<List<Blog>>>,
                response: Response<ApiResponse<List<Blog>>>
            ) {
                if(response.body() == null){
                    Log.d("Profile", "Fail: ${response.code()}")
                    callback.invoke(ApiResponse(response.code(), "", false, null))
                } else {
                    Log.d("Profile", "Success: ${response.body()!!.message} ${response.body()?.data?.size}")
                    callback.invoke(response.body()!!)
                }
                isLoadingProfileSavedPosts = false
            }
        })
    }



    suspend fun getProfileFromDB(id: Int) = profileDao?.getProfile(id)


}