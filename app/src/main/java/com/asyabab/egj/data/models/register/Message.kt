package com.asyabab.egj.data.models.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Message : Serializable {
    @SerializedName("username")
    @Expose
    var username: List<String>? = null

    @SerializedName("email")
    @Expose
    var email: List<String>? = null

}