package com.asyabab.egj.data.models.cart.addkeranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("item_id")
    @Expose
    var itemId: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

}