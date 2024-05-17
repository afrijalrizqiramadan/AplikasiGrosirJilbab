package com.asyabab.egj.data.models.kategori.detailkategori

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Item : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("brand_id")
    @Expose
    var brandId: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

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

    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("is_favorite")
    @Expose
    var isFavorite: Boolean? = null

    @SerializedName("pivot")
    @Expose
    var pivot: Pivot__2? = null

    @SerializedName("images")
    @Expose
    var images: List<Image>? =
        null

    @SerializedName("promo")
    @Expose
    var promo: List<Promo>? =
        null

}