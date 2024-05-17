package com.asyabab.egj.data.models.item.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    interface SearchResponseCallback {
        fun onSuccess(searchResponse: SearchResponse)
        fun onFailure(message: String)
    }
}