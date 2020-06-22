package com.nguyen.string.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nguyen.string.data.authenticationData.AuthData
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.repository.MainRepository

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val registerResponse : MutableLiveData<ApiResponse<AuthData>> = MutableLiveData()
    private val loginResponse : MutableLiveData<ApiResponse<AuthData>> = MutableLiveData()

    val registerStatus: LiveData<Boolean> = Transformations.map(registerResponse){
        Log.d("Register", "sent to ${it.status}")
        it.status
    }

    val registerMessage: LiveData<String> = Transformations.map(registerResponse){
        Log.d("Register", "sent to ${it.message}")
        it.message
    }

    val registerData: LiveData<AuthData> = Transformations.map(registerResponse){
        Log.d("Register", "sent to ${it.data?.email}")

        it.data
    }

    val loginStatus: LiveData<Boolean> = Transformations.map(loginResponse){
        it.status
    }

    val loginMessage: LiveData<String> = Transformations.map(loginResponse){
        it.message
    }

    fun registerUser(name: String, username: String, dob: String, email: String, password: String, confirmPassword: String){
        repository.registerUser(name, username, dob, email, password, confirmPassword, fun(response: ApiResponse<AuthData>){
            registerResponse.value = response
        })
    }

    fun registerWithFb(fbToken: String?){
        repository.registerWithFb(fbToken, fun(response: ApiResponse<AuthData>){

        })
    }

    fun loginUser(email: String, password: String){
        repository.loginUser(email, password, fun(response: ApiResponse<AuthData>){
            loginResponse.value = response
        })
    }

    fun getVerifyEmail() : String?{
        return repository.verificationEmail
    }

}


class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(repository) as T
}