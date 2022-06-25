package com.example.note.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.note.api.UserApi
import com.example.note.models.UserRequest
import com.example.note.models.UserResponse
import com.example.note.utils.Constants
import com.example.note.utils.Constants.TAG
import com.example.note.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi, private val appPref: SharedPreferences) {
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signup(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            storeSession(response.body()!!.token)
        } else if (response.errorBody() != null) {
            //parsing our json
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("something went wrong"))
        }
    }

    private fun storeSession(token: String) {
        val editor = appPref.edit()
        editor.putString(Constants.USER_TOKEN, token)
        editor.apply()
    }
   fun getSession(result:(token: String?)-> Unit){
        val token = appPref.getString(Constants.USER_TOKEN, null)
        result(token)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signin(userRequest)
        handleResponse(response)
    }

    fun logout(result: () -> Unit) {
        val editor = appPref.edit()
        editor.putString(Constants.USER_TOKEN, null)
        editor.apply()
        result()
    }

}
