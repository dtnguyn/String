package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.Blog
import com.nguyen.string.data.User
import com.nguyen.string.repository.MainRepository

class ProfileViewModel(private val repository: MainRepository) : ViewModel() {

    private val userResponse: MutableLiveData<ApiResponse<User>> = MutableLiveData()

    private val userPostsResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val moreUserPostsResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val postDetailResponse: MutableLiveData<ApiResponse<Blog>> = MutableLiveData()

    private val userItinerariesResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val moreUserItinerariesResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()

    private val userSavedPostsResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val moreUserSavedPostsResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()



    var userProfile: LiveData<User> = Transformations.map(userResponse){
        it.data
    }

    var userPosts: LiveData<List<Blog>> = Transformations.map(userPostsResponse){
        it.data
    }

    var moreUserPosts: LiveData<List<Blog>> = Transformations.map(moreUserPostsResponse){
        it.data
    }

    var postDetail: LiveData<Blog> = Transformations.map(postDetailResponse){
        it.data
    }

    var userItineraries: LiveData<List<Blog>> = Transformations.map(userItinerariesResponse){
        it.data
    }

    var moreUserItineraries: LiveData<List<Blog>> = Transformations.map(moreUserItinerariesResponse){
        it.data
    }

    var userSavedPosts: LiveData<List<Blog>> = Transformations.map(userSavedPostsResponse){
        it.data
    }

    var moreSavedUserPosts: LiveData<List<Blog>> = Transformations.map(moreUserSavedPostsResponse){
        it.data
    }


    fun getUserProfile(){
        repository.getUserProfile(fun(response: ApiResponse<User>){
            userResponse.value = response
        })
    }

    fun getUserProfilePosts(){
        repository.getUserProfilePosts(fun(response: ApiResponse<List<Blog>>){
            userPostsResponse.value = response
        })
    }

    fun getMoreUserProfilePosts(){
        repository.getMoreUserProfilePosts(fun(response: ApiResponse<List<Blog>>){
            moreUserPostsResponse.value = response
        })
    }

    fun getPostDetail(postId: Int){
        repository.getPostDetail(postId, fun(response: ApiResponse<Blog>){
            postDetailResponse.value = response
        })
    }

    fun getUserProfileItineraries(){
        repository.getUserProfileItinerary(fun(response: ApiResponse<List<Blog>>){
            userItinerariesResponse.value = response
        })
    }

    fun getMoreUserProfileItineraries(){
        repository.getMoreUserProfileItinerary(fun(response: ApiResponse<List<Blog>>){
            moreUserItinerariesResponse.value = response
        })
    }

    fun getUserProfileSavedPosts(){
        repository.getUserProfileSavedPosts(fun(response: ApiResponse<List<Blog>>){
            userSavedPostsResponse.value = response
        })
    }

    fun getMoreUserProfileSavedPosts(){
        repository.getMoreUserProfileSavedPosts(fun(response: ApiResponse<List<Blog>>){
            moreUserSavedPostsResponse.value = response
        })
    }

}


class ProfileViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ProfileViewModel(repository) as T
}