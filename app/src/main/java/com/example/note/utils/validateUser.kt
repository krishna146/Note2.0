package com.example.note.utils

import android.text.TextUtils
import android.util.Patterns
import com.example.note.models.UserRequest

fun validateCredentials(userRequest: UserRequest, isLogin: Boolean = false): Pair<Boolean, String>{
    var result = Pair(true, "")
    if((!isLogin && TextUtils.isEmpty(userRequest.username) ) || TextUtils.isEmpty(userRequest.email) || TextUtils.isEmpty(userRequest.password)){
        result = Pair(false, "Please Provide the Credentials")
    }
    else if(!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()){
        result = Pair(false, "Please provide valid email")
    }
    else if(userRequest.password.length <= 5){
        result = Pair(false, "Password length should be greater than 5")
    }
    return result

}