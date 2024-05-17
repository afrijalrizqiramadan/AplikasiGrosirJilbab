package com.asyabab.egj.ui.activity.tambahproduk

import android.util.Log
import com.asyabab.egj.data.Repository
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukMVP
import java.util.*

class PilihBarangPresenter(repository: Repository, view: DetailProdukMVP.View) :
    DetailProdukMVP.Presenter {

    private var repository: Repository? = repository
    private var view: DetailProdukMVP.View? = view
    private var productItems = ArrayList<Datum>()

    override fun addOrder(productItem: Datum) {
        if (productItems.size > 0) {
            var same = false
            for (i in productItems.indices) {
                if (productItems[i].id == productItem.id) {
                    productItems[i] = productItem
                    same = true
                }
            }
            if (!same) {
                productItems.add(productItem)
            }
        } else {
            productItems.add(productItem)
        }

        var price = 0

        for (i in productItems.indices) {
            price += productItems[i].harga!!.toInt() * productItems[i].jumlah!!.toInt()
        }
        view!!.updateTotalPrice(price)
        view!!.updateTotalItem(productItems)
//        repository.setProductItems(productItems)
    }

    override fun removeOrder(productItem: Datum) {

        productItems.remove(productItem)

        var price = 0

        for (i in productItems.indices) {
            price += productItems[i].harga!!.toInt() * productItems[i].jumlah!!.toInt()
        }
        view!!.updateTotalPrice(price)
        view!!.updateTotalItem(productItems)
    }

    override fun loadData() {
        view!!.showLoading(true)
        repository?.getKeranjang(
            "1",
            object : KeranjangResponse.KeranjangResponseCallback {
                override fun onSuccess(keranjangResponse: KeranjangResponse) {
                    Log.e("orderr", "onSuccess: ")
                    if (view != null) {
                        view!!.showLoading(false)
                        view!!.upDateList(keranjangResponse)
//                        productItems= ArrayList()
//                        view!!.updateTotalItem(keranjangResponse.data as ArrayList<Data>)
                    }

                }

                override fun onFailure(message: String) {
                    Log.e("orderr", "onFailure: $message")

                    if (view != null) {
                        view!!.showLoading(false)
                        view!!.showError("Gagal memuat")
                    }


                }

            })
    }


    override fun updateTotal() {}
    override fun onDestroy() {}
}