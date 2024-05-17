package com.asyabab.egj.ui.activity.tambahproduk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.localstok
import com.asyabab.egj.utils.*
import kotlinx.android.synthetic.main.activity_tambahstokharga.*
import kotlinx.android.synthetic.main.rv_stok.view.*

class TambahHargaStokActivity : BaseActivity() {
    lateinit var handler: Handler
    private var productItems: ArrayList<localstok>? = null
    var detailItem = com.asyabab.egj.data.models.detailbarang.Datum()

    private var stokhargaAdapter: RecyclerViewAdapter<localstok> = RecyclerViewAdapter(
        R.layout.rv_stok
    ) { view, data, position ->
        view.tvWarna.text = data.warna
        view.tvUkuran.text = data.ukuran
        view.tvHarga.setText(data.harga)
        view.tvStok.setText(data.stok)

        view.tvHarga.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                data.harga = view.tvHarga.text.toString()
            }
        }
        view.tvStok.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                data.stok = view.tvStok.text.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahstokharga)
        rvStokHarga.setVerticalLayout(false)
        rvStokHarga.adapter = stokhargaAdapter

        detailItem =
            intent.getSerializableExtra("stok") as com.asyabab.egj.data.models.detailbarang.Datum

        repository!!.getStok().clear()
        for (i in repository!!.getWarna()) {
            for (j in repository!!.getUkuran()) {
                var lokalstok = localstok()
                lokalstok.id = ""
                lokalstok.warna = i
                lokalstok.ukuran = j
                lokalstok.harga = ""
                lokalstok.stok = ""

                repository!!.getStok().add(lokalstok)
            }
        }
        if (detailItem.idBarang!=null){
            for (i in 0 until repository!!.getStok().size!!) {
                for (t in 0 until detailItem.stok?.size!!) {
                    if (repository!!.getStok()[i].warna==detailItem.stok?.get(t)?.warnaStok && repository!!.getStok()[i].ukuran==detailItem.stok?.get(t)?.ukuranStok ) {
                        repository!!.getStok()[i].harga=detailItem.stok?.get(t)?.hargaStok
                        repository!!.getStok()[i].stok=detailItem.stok?.get(t)?.jumlahStok
                        repository!!.getStok()[i].id=detailItem.stok?.get(t)?.idStok
                        repository!!.getStok()[i].warna?.let { Log.e("testingwarna", it) }
                        repository!!.getStok()[i].ukuran?.let { Log.e("testingukuran", it) }
//                        Log.e("testingharga", repository!!.getStok()[i].harga)
//                        Log.e("testingstok", repository!!.getStok()[i].stok)
                    } else {
//                        repository!!.getStok()[i].harga=""
//                        repository!!.getStok()[i].stok=""
                    }
                }
            }
        }
//
//        for (i in 0 until repository!!.getStok().size!!) {
//            Log.e("testing5"+i, repository!!.getStok()[i].warna)
//            Log.e("testing5"+i, repository!!.getStok()[i].ukuran)
//            Log.e("testing5"+i, repository!!.getStok()[i].harga)
//            Log.e("testing5"+i, repository!!.getStok()[i].stok)
//        }

        stokhargaAdapter.clearItems()
        repository!!.getStok().let { stokhargaAdapter.addItems(it) }
        productItems = repository?.getStok()

        btTambahKategori.onClick {
            rootView.clearFocus()
            if (productItems?.size!! > 0) {
                repository?.saveStok(productItems!!)
                finish()
            } else {
                Toast.makeText(
                    this@TambahHargaStokActivity,
                    "Anda setidaknya harus memilih 1 produk",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



    }


}