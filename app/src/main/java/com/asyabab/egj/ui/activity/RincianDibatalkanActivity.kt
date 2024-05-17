package com.asyabab.egj.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.penjualan.Barang__3
import com.asyabab.egj.data.models.penjualan.Dibatalkan
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.*
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.btBack
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.layoutPembeli
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.rvRincianProduk
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvAlamatPembeli
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvCatatan
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvMetodePembayaran
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvNamaPembeli
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvNomerPembeli
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rinciandibatalkan.tvTotalBiaya
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat

class RincianDibatalkanActivity : BaseActivity() {
    var item = Dibatalkan()
    var id = ""
    private var rincianProdukAdapter: RecyclerViewAdapter<Barang__3> =
        RecyclerViewAdapter(
            R.layout.rv_rincianproduk,
            onBind = { view, data, position ->
                view.tvRincianProdukNama.text = data.namaBarang
                view.tvRincianProdukJumlah.text = data.jumlahPenjualan
                view.tvRincianProdukHarga.text = data.hargaStok
                view.tvRincianProdukKeterangan.text = data.warnaStok+" - "+ data.ukuranStok

            })

    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }
    var dataProfile = com.asyabab.egj.data.models.login.Datum()
    private var stokItems: List<Barang__3>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rinciandibatalkan)

        item = intent.getSerializableExtra("data") as Dibatalkan
        id = item.id.toString()
        tvNoPesanan.text = id
        tvTotalBiaya.text = item.totalHarga.toString()
        tvTanggalPemesanan.text = item.tanggal
        tvMetodePembayaran.text = item.metodepembayaran.toString()
        tvCatatan.text = item.catatan.toString()
        val gson = Gson()

        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )

        btBack.onClick {
            finish()
        }
        if (dataProfile.status=="Admin"){
            btHapusPesanan.visibility=View.VISIBLE
            layoutPembeli.visibility=View.VISIBLE
            tvAlamatPembeli.text=item.user?.get(0)?.alamat
            tvNomerPembeli.text=item.user?.get(0)?.nohp
            tvNamaPembeli.text=item.user?.get(0)?.nama
        }else{
            btHapusPesanan.visibility=View.GONE
        }
        stokItems = item.barang

        btHapusPesanan.onClick {
            item.id?.let { setDeletePenjualan(it) }
        }

        rvRincianProduk.setVerticalLayout(true)
        rvRincianProduk.adapter = rincianProdukAdapter
        rincianProdukAdapter.clearItems()
        item?.barang?.let {
            rincianProdukAdapter.addItems(
                it
            )
        }

    }

    fun setDeletePenjualan(id: String){
        repository?.setDeletePenjualan(
            id,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        launchActivityWithNewTask<MainActivity>()
//                        setResult(10001);
//                        finish()

                    } else {
                        Toast.makeText(applicationContext, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun openWhatsApp(view: View?, phone:String, message:String) {
        try {
            val text = message // Replace with your message.
            val toNumber =
                phone // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setUpdatePenjualan(id: String,
                           produk: String)
    {
        repository?.setUpdatePenjualan(
            id,
            produk,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        launchActivityWithNewTask<MainActivity>()
//                        setResult(10001);
//                        finish()

                    } else {
                        Toast.makeText(applicationContext, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(applicationContext, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }
}