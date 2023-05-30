package com.example.mystoryproject.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryproject.utils.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel (private val pref: SettingsPreferences) : ViewModel() {
    fun getUsername(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun getEmail(): LiveData<String> {
        return pref.getEmail().asLiveData()
    }

    fun getToken(): LiveData<String> {
        return pref.getUserToken().asLiveData()
    }

    fun getUID(): LiveData<String> {
        return pref.getUID().asLiveData()
    }

    fun setUserPreferences(userToken: String, userUID: String, username: String, email: String) {
        viewModelScope.launch {
            pref.saveLoginSession(userToken, userUID, username, email)
            Log.d(userToken, "error")
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch {
            pref.deleteLoginSession()
        }
    }

}