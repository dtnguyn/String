package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.interestData.Interest
import com.nguyen.string.data.userData.User
import com.nguyen.string.repository.MainRepository

class FirstTimeViewModel (private val repository: MainRepository) : ViewModel(){

    private val interestResponse : MutableLiveData<ApiResponse<List<Interest>>> = MutableLiveData()
    private val usersResponse: MutableLiveData<ApiResponse<List<User>>> = MutableLiveData()

    val interestList : LiveData<List<Interest>> = Transformations.map(interestResponse){
        it.data
    }

    val userList : LiveData<List<User>> = Transformations.map(usersResponse){
        it.data
    }

    fun getInterestList(page: String? = null, currentPage: String? = null){
        repository.getInterestList(page, currentPage, fun(response : ApiResponse<List<Interest>>?) {
            interestResponse.value = response
        })
    }

    fun getUserList(page: String? = null, currentPage: String? = null){
        repository.getUserList(page, currentPage, fun(response : ApiResponse<List<User>>?) {
            usersResponse.value = response
        })
    }

    fun followUser(userId: String){
        repository.followUser(userId)
    }
}


class FirstTimeViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FirstTimeViewModel(repository) as T
}