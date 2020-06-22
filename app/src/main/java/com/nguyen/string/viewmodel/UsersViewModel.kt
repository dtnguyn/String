package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.userData.User
import com.nguyen.string.repository.MainRepository

class UsersViewModel (private val repository: MainRepository) : ViewModel(){

    private val userResponse : MutableLiveData<ApiResponse<List<User>>> = MutableLiveData()

    val userList : LiveData<List<User>> = Transformations.map(userResponse){
        it.data
    }

    fun getUserList(page: String? = null, currentPage: String? = null){
        repository.getUserList(page, currentPage, fun(response : ApiResponse<List<User>>?) {
            userResponse.value = response
        })
    }
}


class UsersViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FirstTimeViewModel(repository) as T
}