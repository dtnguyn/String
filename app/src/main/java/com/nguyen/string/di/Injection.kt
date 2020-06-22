package com.nguyen.string.di

import androidx.lifecycle.ViewModelProvider
import com.nguyen.string.repository.MainRepository
import com.nguyen.string.viewmodel.FirstTimeViewModelFactory
import com.nguyen.string.viewmodel.MainViewModelFactory

object Injection {


    fun provideMainViewModelFactory() : ViewModelProvider.Factory{
        return MainViewModelFactory(provideMainRepository())
    }

    fun provideInterestViewModel() : ViewModelProvider.Factory{
        return FirstTimeViewModelFactory(provideMainRepository())
    }

    private fun provideMainRepository(): MainRepository {
        return MainRepository
    }
}