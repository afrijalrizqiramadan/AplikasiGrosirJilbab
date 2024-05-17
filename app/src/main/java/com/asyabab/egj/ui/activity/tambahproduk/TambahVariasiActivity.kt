package com.asyabab.egj.ui.activity.tambahproduk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.utils.AndroidBug5497Workaround
import com.asyabab.egj.utils.RecyclerViewAdapter
import com.asyabab.egj.utils.onClick
import com.asyabab.egj.utils.setVerticalLayout
import kotlinx.android.synthetic.main.activity_tambahvariasi.*
import kotlinx.android.synthetic.main.list_simple.view.*
import kotlinx.android.synthetic.main.list_spinner.view.text1

var detailItem = com.asyabab.egj.data.models.detailbarang.Datum()

class TambahVariasiActivity : BaseActivity() {
    lateinit var handler: Handler
    private var warnaAdapter: RecyclerViewAdapter<String> = RecyclerViewAdapter(
        R.layout.list_simple,
        onBind = { view, data, position ->
            view.text1.text = data

            view.onClick {

            }
            view.btHapus.onClick {
                hapusWarna(position)
            }
        })
    private var ukuranAdapter: RecyclerViewAdapter<String> = RecyclerViewAdapter(
        R.layout.list_simple,
        onBind = { view, data, position ->
            view.text1.text = data

            view.onClick {

            }
            view.btHapus.onClick {
                hapusUkuran(position)
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahvariasi)
//        AndroidBug5497Workaround.assistActivity(this, R.id.content_area)

//        val adapterwarna = ArrayAdapter(this, android.R.layout.simple_list_item_1,  repository!!.getWarna())
//        rvWarna.adapter = adapterwarna
//
//        val adapterukuran = ArrayAdapter(this, android.R.layout.simple_list_item_1, repository!!.getUkuran())
//        rvUkuran.adapter = adapterukuran
        try {
            detailItem = intent.getSerializableExtra("data") as com.asyabab.egj.data.models.detailbarang.Datum

        } catch (e: Exception) {
            Log.d("Gagal", e.toString())

        }
        ukuranAdapter.clearItems()
        rvUkuran.setVerticalLayout(false)
        rvUkuran.adapter = ukuranAdapter
        repository?.let { ukuranAdapter.addItems(it.getUkuran()) }

        warnaAdapter.clearItems()
        rvWarna.setVerticalLayout(false)
        rvWarna.adapter = warnaAdapter
        repository?.let { warnaAdapter.addItems(it.getWarna()) }

//        detailItem.data?.get(0)?.selesai?.let { warnaAdapter.addItems(it) }
//        penjualanResponse.data?.get(0)?.selesai?.let { ukuranAdapter.addItems(it) }
        btSelesai.onClick {
            if (repository!!.getUkuran().size>0&&repository!!.getUkuran().size>0){
                finish()
            }else {
                Toast.makeText(
                    context,
                    "Masukkan Varian Warna dan Ukuran",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btBack.onClick {
            if (repository!!.getUkuran().size>0&&repository!!.getUkuran().size>0){
                finish()
            }else {
                Toast.makeText(
                    context,
                    "Masukkan Varian Warna dan Ukuran",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btTambahWarna.onClick {
            if (inputWarna.text.toString() == "") {
                Toast.makeText(applicationContext, "Kosong", Toast.LENGTH_LONG)
                    .show()
            } else {
                repository!!.getWarna().add(inputWarna.text.toString())
                warnaAdapter.clearItems()
                repository?.let { warnaAdapter.addItems(it.getWarna()) }
                inputWarna.setText("")
            }
        }

        btTambahUkuran.onClick {
            if (inputUkuran.text.toString() == "") {
                Toast.makeText(applicationContext, "Kosong", Toast.LENGTH_LONG)
                    .show()
            } else {
                repository!!.getUkuran().add(inputUkuran.text.toString())
                ukuranAdapter.clearItems()
                repository?.let { ukuranAdapter.addItems(it.getUkuran()) }
                inputUkuran.setText("")

            }
        }
    }

    fun hapusWarna(nama:Int) {
        repository!!.getWarna().removeAt(nama)
        warnaAdapter.clearItems()
        warnaAdapter.addItems(repository!!.getWarna())
    }
    override fun onBackPressed() {

        if (repository!!.getUkuran().size>0&&repository!!.getUkuran().size>0){
            super.onBackPressed()
        }else {
            Toast.makeText(
                this,
                "Masukkan Varian Warna dan Ukuran",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    fun hapusUkuran(nama:Int) {
        repository!!.getUkuran().removeAt(nama)
        ukuranAdapter.clearItems()
        ukuranAdapter.addItems(repository!!.getUkuran())
    }
}