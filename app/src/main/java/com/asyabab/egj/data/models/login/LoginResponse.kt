package com.asyabab.egj.data.models.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    interface LoginResponseCallback {
        fun onSuccess(loginResponse: LoginResponse)
        fun onFailure(message: String)
    }
    
    
}