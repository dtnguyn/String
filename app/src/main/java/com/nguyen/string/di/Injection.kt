package com.nguyen.string.di

import androidx.lifecycle.ViewModelProvider
import com.nguyen.string.repository.MainRepository
import com.nguyen.string.viewmodel.FeedViewModel
import com.nguyen.string.viewmodel.FeedViewModelFactory
import com.nguyen.string.viewmodel.FirstTimeViewModelFactory
import com.nguyen.string.viewmodel.MainViewModelFactory

object Injection {


    fun provideMainViewModelFactory() : ViewModelProvider.Factory{
        return MainViewModelFactory(provideMainRepository())
    }

    fun provideInterestViewModel() : ViewModelProvider.Factory{
        return FirstTimeViewModelFactory(provideMainRepository())
    }

    fun provideFeedViewModel() : ViewModelProvider.Factory{
        return FeedViewModelFactory(provideMainRepository())
    }

    private fun provideMainRepository(): MainRepository {
        return MainRepository
    }
}