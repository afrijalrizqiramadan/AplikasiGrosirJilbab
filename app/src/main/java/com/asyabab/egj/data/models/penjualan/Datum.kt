package com.asyabab.egj.data.models.penjualan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Datum : Serializable {
    @SerializedName("proses")
    @Expose
    var proses: List<Prose>? = null

    @SerializedName("kirim")
    @Expose
    var kirim: List<Kirim>? = null

    @SerializedName("selesai")
    @Expose
    var selesai: List<Selesai>? = null

    @SerializedName("batal")
    @Expose
    var dibatalkan: List<Dibatalkan>? = null
}