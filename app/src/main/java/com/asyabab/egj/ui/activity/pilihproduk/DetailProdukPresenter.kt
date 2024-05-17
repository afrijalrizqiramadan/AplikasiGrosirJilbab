package com.asyabab.egj.ui.activity.cart

import android.util.Log
import com.asyabab.egj.data.Repository
import com.asyabab.egj.data.models.detailbarang.DetailBarangResponse
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukMVP
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DetailProdukPresenter(repository: Repository, view: DetailProdukMVP.View) :
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
    fun getItemDetail(id: String) {

        repository!!.getDetailItem(
            id,
            object : DetailBarangResponse.DetailBarangResponseCallback {
                override fun onSuccess(detailBarangResponse: DetailBarangResponse) {
                    if (detailBarangResponse.status == true) {
//                        val jsonObject = KeranjangResponse()
                        val jsonObject = JSONObject()
                        jsonObject.put("status", "")
                        jsonObject.put("message", "")
                        try {
                            val jsonArray = JSONArray()

                            for (i in detailBarangResponse.data?.get(0)?.stok?.indices!!) {
                                val jsonObj = JSONObject()
//                                jsonObject.data?.get(i)?.id =detailBarangResponse.data?.get(0)?.stok!![i].idStok
//                                jsonObject.data?.get(i)?.idbarang =detailBarangResponse.data!![i].idBarang
//                                jsonObject.data?.get(i)?.nama =detailBarangResponse.data!![i].namaBarang
//                                jsonObject.data?.get(i)?.warna =detailBarangResponse.data?.get(0)?.stok!![i].warnaStok
//                                jsonObject.data?.get(i)?.ukuran =detailBarangResponse.data?.get(0)?.stok!![i].ukuranStok
//                                jsonObject.data?.get(i)?.harga =detailBarangResponse.data?.get(0)?.stok!![i].hargaStok
//                                jsonObject.data?.get(i)?.jumlah =""
//                                jsonObject.data?.get(i)?.inventory =detailBarangResponse.data?.get(0)?.stok!![i].jumlahStok
//                                jsonObject.data?.get(i)?.gambar =detailBarangResponse.data?.get(0)?.gambar!![0].namaGambar

                                jsonObj.put("id", detailBarangResponse.data?.get(0)?.stok!![i].idStok)
                                jsonObj.put("idbarang", detailBarangResponse.data!![0].idBarang)
                                jsonObj.put("nama", detailBarangResponse.data!![0].namaBarang)
                                jsonObj.put("warna", detailBarangResponse.data?.get(0)?.stok!![i].warnaStok)
                                jsonObj.put("ukuran", detailBarangResponse.data?.get(0)?.stok!![i].ukuranStok)
                                jsonObj.put("harga", detailBarangResponse.data?.get(0)?.stok!![i].hargaStok)
                                jsonObj.put("jumlah", 1)
                                jsonObj.put("inventory", detailBarangResponse.data?.get(0)?.stok!![i].jumlahStok)
                                jsonObj.put("gambar", detailBarangResponse.data?.get(0)?.gambar!![0].namaGambar)
//
                                jsonArray.put(jsonObj)
                            }
                            jsonObject.put("data", jsonArray)

                            val jsonObject = jsonObject.toString()
                            val chatModel = Gson().fromJson(jsonObject, KeranjangResponse::class.java)

//                            jsonObject(jsonArray)
                            view!!.upDateList(chatModel)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Log.e("hasiljson", jsonObject.toString())
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


    override fun loadData() {
//        view!!.showLoading(true)
//        repository?.getKeranjang(
//            "1",
//            object : KeranjangResponse.KeranjangResponseCallback {
//                override fun onSuccess(keranjangResponse: KeranjangResponse) {
//                    Log.e("orderr", "onSuccess: ")
//                    if (view != null) {
//                        view!!.showLoading(false)
//                        view!!.upDateList(keranjangResponse)
//
//
////                        productItems= ArrayList()
////                        view!!.updateTotalItem(keranjangResponse.data as ArrayList<Data>)
//                    }
//
//                }
//
//                override fun onFailure(message: String) {
//                    Log.e("orderr", "onFailure: $message")
//
//                    if (view != null) {
//                        view!!.showLoading(false)
//                        view!!.showError("Gagal memuat")
//                    }
//
//
//                }
//
//            })
    }


    override fun updateTotal() {}
    override fun onDestroy() {}
}