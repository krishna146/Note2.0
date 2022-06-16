package com.example.note.repository

import android.util.Log
import com.example.note.api.UserApi
import com.example.note.models.UserRequest
import com.example.note.utils.Constants
import com.example.note.utils.Constants.TAG
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    suspend fun registerUser(userRequest: UserRequest){
        val response = userApi.signup(userRequest)
        Log.d(TAG, response.body().toString())
    }
    suspend fun loginUser(userRequest: UserRequest){
        val response = userApi.signin(userRequest)
        Log.d(TAG, response.body().toString())
    }
}