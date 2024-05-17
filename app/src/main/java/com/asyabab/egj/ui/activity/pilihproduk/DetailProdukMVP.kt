package com.asyabab.egj.ui.activity.pilihproduk


import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import java.util.*

class DetailProdukMVP {
    interface View {
        fun showLoading(isLoading: Boolean)
        fun showError(message: String?)
        fun upDateList(productItems: KeranjangResponse)
        fun updateTotalItem(productItems: ArrayList<Datum>)

        fun updateTotalPrice(value: Int)
    }

    interface Presenter {
        fun loadData()
        fun onDestroy()
        fun addOrder(productItem: Datum)
        fun updateTotal()
        fun removeOrder(productItem: Datum)
    }
}