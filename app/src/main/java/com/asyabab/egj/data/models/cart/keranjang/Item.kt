package com.asyabab.egj.data.models.cart.keranjang

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Item : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("desctription")
    @Expose
    var desctription: String? = null

    @SerializedName("price")
    @Expose
    var price: Int? = null

    @SerializedName("variant")
    @Expose
    var variant: List<String>? = null

    @SerializedName("inventory")
    @Expose
    var inventory: Int? = null

    @SerializedName("weight")
    @Expose
    var weight: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? = null

    override fun toString(): String {
        return "$name"
    }

}