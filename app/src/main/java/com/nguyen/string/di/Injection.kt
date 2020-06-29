package com.nguyen.string.di

import androidx.lifecycle.ViewModelProvider
import com.nguyen.string.repository.MainRepository
import com.nguyen.string.viewmodel.*

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

    fun provideProfileViewModel() : ViewModelProvider.Factory{
        return ProfileViewModelFactory(provideMainRepository())
    }

    private fun provideMainRepository(): MainRepository {
        return MainRepository
    }
}