package com.example.wallpaper

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.wallpaper.utils.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {
    private val _showLoader= MutableLiveData<Boolean>()

    val showLoader: LiveData<Boolean> = _showLoader

    private val _showNotification= MutableLiveData<SingleLiveEvent<String>>()
    val showNotification: LiveData<SingleLiveEvent<String>> = _showNotification
    private val _showMessage= MutableLiveData<SingleLiveEvent<String>>()
    val showMessage: LiveData<SingleLiveEvent<String>> = _showMessage



    fun showLoader(){
        _showLoader.value=true
    }

    fun hideLoader(){
        _showLoader.value=false
    }

    fun showMessage(message: String ?){
        if(!message.isNullOrEmpty()){
            _showMessage.value= SingleLiveEvent(message)
        }
    }

    fun showNotification(message: String ?){
        if(!message.isNullOrEmpty()){
            _showNotification.value= SingleLiveEvent(message)
        }
    }



}
