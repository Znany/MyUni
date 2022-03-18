package com.hfad.myuni.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConnectionViewModel: ViewModel() {
    private val isConnected = MutableLiveData<Boolean>()

    fun getIsConnected(): MutableLiveData<Boolean>{
        return isConnected
    }

    fun isConnected(isConnected: Boolean) {
        this.isConnected.postValue(isConnected)
    }
}