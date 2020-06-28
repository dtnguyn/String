package com.nguyen.string.viewmodel

import androidx.lifecycle.*
import com.nguyen.string.data.ApiResponse
import com.nguyen.string.data.Blog
import com.nguyen.string.data.Comment
import com.nguyen.string.repository.MainRepository

class FeedViewModel(private val repository: MainRepository) : ViewModel() {

    private val feedResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()
    private val moreFeedResponse: MutableLiveData<ApiResponse<List<Blog>>> = MutableLiveData()

    private val commentsResponse: MutableLiveData<ApiResponse<List<Comment>>> = MutableLiveData()
    private val moreCommentsResponse: MutableLiveData<ApiResponse<List<Comment>>> = MutableLiveData()


    private var currentPage: Int = 0

    var blogList: LiveData<List<Blog>> = Transformations.map(feedResponse){
        it.data
    }

    var moreBlogList: LiveData<List<Blog>> = Transformations.map(moreFeedResponse){
        it.data
    }

    var code: LiveData<Int> = Transformations.map(feedResponse){
        it.code
    }

    var commentList: LiveData<List<Comment>> = Transformations.map(commentsResponse){
        it.data
    }

    var moreCommentList: LiveData<List<Comment>> = Transformations.map(moreCommentsResponse){
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

    fun like(id: Int){
        repository.like(id)
    }

    fun save(id: Int){
        repository.save(id)
    }

    fun getComments(id: Int){
        repository.getComments(id, fun(response: ApiResponse<List<Comment>>){
            commentsResponse.value = response
        })
    }

    fun getMoreComments(id: Int){
        repository.getMoreComments(id, fun(response: ApiResponse<List<Comment>>){
            moreCommentsResponse.value = response
        })
    }

    fun addComment(id: Int, comment: String, callback: (result: Boolean) -> Unit){
        repository.addComment(id, comment, fun(_result: Boolean){
            callback.invoke(_result)
        })
    }

}


class FeedViewModelFactory(private val repository: MainRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = FeedViewModel(repository) as T
}