package com.asyabab.egj.ui.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.checkout.CheckoutResponse
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukActivity
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_rincianprofil.btBack
import kotlinx.android.synthetic.main.popup_cancel.btApply
import kotlinx.android.synthetic.main.popup_konfirmasi.*
import kotlinx.android.synthetic.main.rv_checkout.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class CheckoutActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var jenisSpinner: Spinner? = null
    var dataProfile = com.asyabab.egj.data.models.login.Datum()
    var item = ""

    private var productItems: ArrayList<Datum>? = null
    private var price = 0
    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")

        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }

    private val jenis =
        arrayOf("Bayar di Toko", "COD", "Transfer Bank")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val gson = Gson()


        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )

        jenisSpinner = findViewById(R.id.inputPembayaran)
        jenisSpinner?.onItemSelectedListener = this

        val adapterJenis = ArrayAdapter(
            this,
            R.layout.list_spinner, R.id.text1,
            jenis
        )
        adapterJenis.setDropDownViewResource(R.layout.list_spinner)
        jenisSpinner?.adapter = adapterJenis

        loading?.setCancelable(true)
        productItems = repository?.getProduvt()
        try {
            item = intent.getStringExtra("data")!!

            Log.d("Berhasil", item)

        } catch (e: Exception) {
            Log.d("Gagal", e.toString())

        }
        Log.d("cook55", productItems?.size.toString())
        rvTasBelanja.setVerticalLayout(true)
        rvTasBelanja.adapter = tasBelanjaAdapter
        tasBelanjaAdapter.clearItems()
        if (productItems?.isNotEmpty() == true) {
            tasBelanjaAdapter.addItems(productItems!!)

            for (i in productItems!!.indices) {
                price += productItems!![i].harga!!.toInt() * productItems!![i].jumlah!!.toInt()
            }
            tv_subtotal.text = price.convertRupiah()
        }

        val status = jenisSpinner!!.selectedItem.toString()

        btBack.onClick {
            finish()
        }
        val sdf = SimpleDateFormat("yyMMdd")

        btBayar.onClick {

            loading!!.show()
            val jsonObject = JSONObject()
            try {
                val jsonArray = JSONArray()

                for (i in productItems!!.indices) {
                    val jsonObj = JSONObject()
                    jsonObj.put("item_id", productItems!![i].idbarang)
                    jsonObj.put("name", productItems!![i].nama)
                    jsonObj.put("price", productItems!![i].harga)
                    jsonObj.put("qty", productItems!![i].jumlah)
                    jsonObj.put("ukuran", productItems!![i].ukuran)
                    jsonObj.put("warna", productItems!![i].warna)
                    jsonObj.put("total", (productItems!![i].harga!!.toInt() * productItems!![i].jumlah!!.toInt())
                    )
                    jsonArray.put(jsonObj)
                }


                jsonObject.put("item", jsonArray)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.e("hasil", jsonObject.toString())

            repository?.checkout(
                "",
                dataProfile.id!!,
                tv_subtotal.text.toString(),
                inputCatatan.text.toString(),
                jenisSpinner!!.selectedItem.toString(),
                jsonObject.toString(),
                object : CheckoutResponse.CheckoutResponseCallback {
                    override fun onSuccess(checkoutResponse: CheckoutResponse) {
                        loading!!.dismiss()
                        if (checkoutResponse.status == true) {
                            if (item !=null){
                                for (i in productItems!!.indices) {
                                    var a=productItems!![i].id
                                    deleteKeranjang(a!!)
                                }
                            }
                            repository?.saveCheckoutResponse(checkoutResponse)
                            val dialog = Dialog(context)
                            dialog.apply {
                                requestWindowFeature(Window.FEATURE_NO_TITLE)
                                setContentView(R.layout.popup_konfirmasi)
                                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                window?.setLayout(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                                )
                                btHome.setOnClickListener{
                                                                        launchActivityWithNewTask<MainActivity>()

                                }
                                btApply.setOnClickListener {
                                    launchActivityWithNewTask<MainActivity>()

                                    var mPhoneNumber = "+6285733331617"
                                    val mMessage = "Hello Admin Saya "+dataProfile.name+" mau mengonfirmasi pesanan saya"
                                    val mSendToWhatsApp =
                                        "https://wa.me/$mPhoneNumber?text=$mMessage"
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(
                                                mSendToWhatsApp
                                            )
                                        )
                                    )

//                                    dismiss()
                                }

                                show()
                            }
                        } else {
                            Log.e("hasil", "gagal")
                        }
                    }

                    override fun onFailure(message: String) {
                        loading!!.dismiss()
                        Log.e("hasil", message)
                    }

                })
        }
    }

    fun deleteKeranjang(itemId: String) {
        repository!!.deleteKeranjang(
            itemId,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                    }
                }

                override fun onFailure(message: String) {
//                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
//                        .show()
                }

            })
    }
    private var tasBelanjaAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_checkout
    ) { view, data, position ->

        view.tvKeranjangNama.text = data.nama
        view.tvQty.text = data.jumlah.toString()
        data.gambar?.let {
            view.tvKeranjangGambar.loadImageFromResources(
                this,
                it
            )
        }
        view.tvKeranjangHarga.text =
            (data?.harga!!.toInt()).convertRupiah()
        view.tvKeranjangHargaSub.text =
            (data?.harga!!.toInt()*data.jumlah!!.toInt()).convertRupiah()
        view.onClick {
            launchActivity<DetailProdukActivity> {
                putExtra("data", data.id.toString())
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}