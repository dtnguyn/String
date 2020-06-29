package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.data.User
import com.nguyen.string.databinding.FragmentFeedBinding
import com.nguyen.string.databinding.FragmentProfileBinding
import com.nguyen.string.repository.MainRepository

class ProfileViewModel(private val repository: MainRepository) : ViewModel() {

    private val userResponse: MutableLiveData<ApiResponse<User>> = MutableLiveData()
    private val userPostsResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()

    var userProfile: LiveData<User> = Transformations.map(userResponse){
        it.data
    }

    var userPosts: LiveData<List<Blog>> = Transformations.map(userPostsResponse){
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


}


class ProfileViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ProfileViewModel(repository) as T
}