package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.Blog
import com.nguyen.string.repository.MainRepository

class FeedViewModel(private val repository: MainRepository) : ViewModel() {

    private val feedResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val moreFeedResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()


    private var currentPage: Int = 0

    var blogList: LiveData<List<Blog>> = Transformations.map(feedResponse){
        it.data
    }

    var moreBlogList: LiveData<List<Blog>> = Transformations.map(moreFeedResponse){
        it.data
    }

    fun getFeed(){
        repository.getFeed(fun(response: ApiResponse<List<Blog>>?){
            feedResponse.value = response
        })
    }

    fun loadMoreFeed(){
        currentPage++
        repository.getMoreFeed(fun(response: ApiResponse<List<Blog>>?){
            moreFeedResponse.value = response
        })
    }

}


class FeedViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FeedViewModel(repository) as T
}