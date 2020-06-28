package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.Interest
import com.nguyen.string.data.User
import com.nguyen.string.repository.MainRepository

class FirstTimeViewModel (private val repository: MainRepository) : ViewModel(){

    private val interestResponse : MutableLiveData<ApiResponse<List<Interest>>> = MutableLiveData()
    private val usersResponse: MutableLiveData<ApiResponse<List<User>>> = MutableLiveData()
    private val moreUserResponse: MutableLiveData<ApiResponse<List<User>>> = MutableLiveData()

    val interestList : LiveData<List<Interest>> = Transformations.map(interestResponse){
        it.data
    }

    val userList : LiveData<List<User>> = Transformations.map(usersResponse){
        it.data
    }

    val moreUserList : LiveData<List<User>> = Transformations.map(usersResponse){
        it.data
    }

    fun getInterestList(page: String? = null, currentPage: String? = null){
        repository.getInterestList(page, currentPage, fun(response : ApiResponse<List<Interest>>?) {
            interestResponse.value = response
        })
    }

    fun getUserList(){
        repository.getUserList(fun(response : ApiResponse<List<User>>?) {
            usersResponse.value = response
        })
    }

    fun getMoreUserList(){
        repository.getMoreUserList {
            moreUserResponse.value = it
        }
    }


    fun followUser(userId: String){
        repository.followUser(userId)
    }

    fun submitSelectedInterestList(interests: List<Interest>){
        repository.submitSelectedInterestList(interests)
    }

}


class FirstTimeViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FirstTimeViewModel(repository) as T
}