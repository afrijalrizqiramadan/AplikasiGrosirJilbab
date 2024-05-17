package com.asyabab.egj.ui.activity.cart

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.ui.activity.CheckoutActivity
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukActivity
import com.asyabab.egj.utils.*
import kotlinx.android.synthetic.main.activity_tasbelanja.*
import kotlinx.android.synthetic.main.activity_tasbelanja.btBack
import kotlinx.android.synthetic.main.rv_pilihstok.view.*
import kotlinx.android.synthetic.main.rv_tasbelanja.*
import kotlinx.android.synthetic.main.rv_tasbelanja.view.*
import kotlinx.android.synthetic.main.rv_tasbelanja.view.btMin
import kotlinx.android.synthetic.main.rv_tasbelanja.view.btPlus
import kotlinx.android.synthetic.main.rv_tasbelanja.view.cbItem
import kotlinx.android.synthetic.main.rv_tasbelanja.view.tvQty
import java.text.DecimalFormat
import java.util.*


class TasBelanjaActivity : BaseActivity(), TasBelanjaMVP.View {

    private var presenter: TasBelanjaPresenter? = null
    private var productItem = ArrayList<Datum>()
    private var tasBelanjaAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_tasbelanja
    ) { view, data, position ->

        view.tvKeranjangNama.text = data.nama
        view.tvQty.setText(data.jumlah)
        data.gambar?.let {
            view.tvKeranjangGambar.loadImageFromResources(
                this,
                it
            )
        }
        view.tvVariasi.text=data.warna+" - "+data.ukuran
        view.tvQty.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if(view.tvQty.text.toString().toInt()> data.inventory?.toInt()!!){
                    Toast.makeText(this, "Jumlah Melebihi Stok", Toast.LENGTH_SHORT).show()
                    view.tvQty.setText(data.inventory.toString())
                }else if(view.tvQty.text.toString().toInt()<=0){
                    view.tvQty.setText("1")
                }
//                setUpdateJumlah(
//                    data.id.toString(),
//                    tvQty.text.toString()
//                )
            }
        }
        var icounter = data.jumlah?.toInt()

        view.btPlus.onClick {
//            var icounter = data.jumlah?.toInt()
            Log.d("posisi", icounter.toString())
            view.btMin.isClickable = true

            if (icounter != null) {
                if (icounter < data.inventory!!.toInt()) {
                    icounter += 1
                    Log.d("posisi+", icounter.toString())
                    setUpdateJumlah(data.id.toString(), icounter.toString())
                    view.tvQty.setText(icounter.toString())
                } else {
                    view.btPlus.isClickable = false
                }
            }
        }
        view.btMin.onClick {
            var temp = true
            Log.d("posisi", icounter.toString())
            view.btPlus.isClickable = true

            if (icounter != null) {
                if (icounter != 1) {
                    icounter -= 1
                    Log.d("posisi-", icounter.toString())
                    setUpdateJumlah(data.id.toString(), icounter.toString())
                    view.tvQty.setText(icounter.toString())
                } else {
                    view.btMin.isClickable = false

                }
            }
        }
        view.cbItem.isChecked = RecyclerViewAdapter.isSelectedAll == true
        view.cbItem.setOnCheckedChangeListener { buttonView, p1 ->
            if (p1) {
                presenter?.addOrder(data)
            } else {
                presenter?.removeOrder(data)
            }
        }
        view.btHapusKeranjang.onClick {
            deleteKeranjang(data.id.toString())
        }
        view.tvKeranjangHarga.text = data.harga?.toInt()?.convertRupiah()
        view.onClick {
            launchActivity<DetailProdukActivity> {
                putExtra("data", data.idbarang.toString())
            }
        }

        view.tvQty.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.HIDE_IMPLICIT_ONLY,
                    0
                )
                true
            } else false
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasbelanja)
        btBack.onClick {
            finish()
        }

        rvTasBelanja.setVerticalLayout(true)
        rvTasBelanja.adapter = tasBelanjaAdapter

        presenter = TasBelanjaPresenter(repository!!, this)
        presenter?.loadData()

        cbAll.setOnClickListener {
            if (cbAll.isChecked) {
                tasBelanjaAdapter.selectAll()
            } else {
                tasBelanjaAdapter.unselectall()

            }
        }

        swipeResfresh.setOnRefreshListener {
            presenter?.loadData()
            swipeResfresh.isRefreshing = false

        }

        Log.d("cook1", productItem.size.toString())
        btCheckout.onClick {
            if (productItem.size > 0) {
                repository?.saveProduct(productItem)
                launchActivity<CheckoutActivity> {
                    putExtra("data", 1)
                }
            } else {
                Toast.makeText(
                    this@TasBelanjaActivity,
                    "Anda setidaknya harus memilih 1 produk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    fun deleteKeranjang(itemId: String) {
        repository!!.deleteKeranjang(
            itemId,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        presenter?.loadData()
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun setUpdateJumlah(id: String, qty: String): Boolean {
        var temp = false
        repository!!.setUpdateJumlah(
            id,
            qty,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        presenter?.loadData()
                        temp = true
                        if (generalResponse.status == false) {
                            Toast.makeText(
                                applicationContext,
                                "Jumlah Melebihi Stok",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                    temp = false

                }

            })
        return temp
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading!!.show()
        } else {
            loading!!.dismiss()
        }
    }

    override fun showError(message: String?) {
        if (message=="kosong"){
            panelKosong.visibility = View.VISIBLE
            frameshimmer.visibility = View.GONE
            framemain.visibility=View.VISIBLE
            rvTasBelanja.visibility = View.GONE

        }
//        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
//            .show()
    }

    override fun upDateList(
        productItems: KeranjangResponse
    ) {
        if (productItems.status == true) {
            if (productItems.data!!.size.toString() == "0") {
                panelKosong.visibility = View.VISIBLE
                rvTasBelanja.visibility = View.GONE
                panelBottom.visibility = View.INVISIBLE
            } else {
                panelKosong.visibility = View.GONE
                rvTasBelanja.visibility = View.VISIBLE
                panelBottom.visibility = View.VISIBLE
                tasBelanjaAdapter.clearItems()
                tasBelanjaAdapter.addItems(productItems.data!!)
                tvJumlahItem.text =
                    "Item Anda (" + productItems.data!!.size.toString() + ")"
            }
            framemain.visibility = View.VISIBLE
            frameshimmer.visibility = View.GONE
        }
    }

    override fun updateTotalItem(productItems: ArrayList<Datum>) {
        productItem = productItems
        Log.d("cook2", productItem.size.toString())
    }

    override fun updateTotalPrice(value: Int) {
        tv_SubTotal.text = value.convertRupiah()
    }

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,###")

        val strFormat = df.format(this)
        var bilangan = "Rp. " + strFormat
        return bilangan
    }
}
