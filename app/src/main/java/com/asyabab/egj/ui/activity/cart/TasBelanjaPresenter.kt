package com.asyabab.egj.ui.activity.cart

import android.util.Log
import com.asyabab.egj.data.Repository
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukMVP
import com.google.gson.Gson
import java.util.*

class TasBelanjaPresenter(repository: Repository, view: TasBelanjaMVP.View) :
    DetailProdukMVP.Presenter {
    var dataProfile = com.asyabab.egj.data.models.login.Datum()

    private var repository: Repository? = repository
    private var view: TasBelanjaMVP.View? = view
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
        val gson = Gson()

        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )

        view!!.showLoading(true)
        repository?.getKeranjang(
            dataProfile.id!!,
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
                        view!!.showError("kosong")
                    }


                }

            })
    }


    override fun updateTotal() {}
    override fun onDestroy() {}
}