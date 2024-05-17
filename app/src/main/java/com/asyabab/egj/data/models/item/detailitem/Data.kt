package com.asyabab.egj.data.models.item.detailitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable {
    @SerializedName("item")
    @Expose
    var item: Item? = null

    @SerializedName("s_item")
    @Expose
    var sItem: List<SItem>? = null

}