package com.example.budgetcontrolandroid

import android.app.Application
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var tokenRepository: TokenRepository


    override fun onTerminate() {
        super.onTerminate()
        tokenRepository.onDestroy()
    }
}