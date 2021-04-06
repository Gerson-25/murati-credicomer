package com.example.creditscomer.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class VerificationViewModel(application: Application):BaseViewModel(application) {

    var _isReal =MutableLiveData<List<Boolean>>()
    val isReal:LiveData<List<Boolean>>
    get() = _isReal

    private var _hasOffer =MutableLiveData<Boolean>()
    val hasOffer:LiveData<Boolean>
        get() = _hasOffer

    private var _user =MutableLiveData<String>()
    val user:LiveData<String>
        get() = _user

    private var _loading =MutableLiveData<Boolean>()
    val loading:LiveData<Boolean>
        get() = _loading

    private var _error =MutableLiveData<Boolean>()
    val error:LiveData<Boolean>
        get() = _error




    fun saveID(id: String, hasOffer:Boolean){
        _user.value = id
        _hasOffer.value = hasOffer
    }

    fun verifyOffer():Boolean{
        return _hasOffer.value!!
    }

}