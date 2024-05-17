package com.asyabab.egj.data.models.cart.keranjang

import com.asyabab.egj.data.models.item.detailitem.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("item")
    @Expose
    var item: Item? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null

    @SerializedName("qty")
    @Expose
    var qty: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null


}