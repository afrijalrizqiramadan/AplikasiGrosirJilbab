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
import android.widget.RelativeLayout
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.penjualan.Barang__1
import com.asyabab.egj.utils.*
import com.asyabab.egj.data.models.penjualan.Kirim
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rinciandikirim.*
import kotlinx.android.synthetic.main.activity_rinciandikirim.btBack
import kotlinx.android.synthetic.main.activity_rinciandikirim.layoutPembeli
import kotlinx.android.synthetic.main.activity_rinciandikirim.rvRincianProduk
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvAlamatPembeli
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvCatatan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvMetodePembayaran
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvNamaPembeli
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvNoPesanan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvNomerPembeli
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvTanggalPemesanan
import kotlinx.android.synthetic.main.activity_rinciandikirim.tvTotalBiaya
import kotlinx.android.synthetic.main.popup_cancel.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import java.text.DecimalFormat

class RincianDikirimActivity : BaseActivity() {
    var item = Kirim()
    var id = ""
    private var rincianProdukAdapter: RecyclerViewAdapter<Barang__1> =
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
    private var stokItems: List<Barang__1>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rinciandikirim)
        item = intent.getSerializableExtra("data") as Kirim
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
            btSelesai.visibility=View.VISIBLE
            btChatAdmin.visibility=View.GONE
            layoutPembeli.visibility=View.VISIBLE
            tvAlamatPembeli.text=item.user?.get(0)?.alamat
            tvNomerPembeli.text=item.user?.get(0)?.nohp
            tvNamaPembeli.text=item.user?.get(0)?.nama
        }else{
            btSelesai.visibility=View.VISIBLE
            btChatAdmin.visibility=View.VISIBLE

        }
        stokItems = item.barang

        btSelesai.onClick {
            val dialog = Dialog(context)
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.popup_cancel)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                if (tvTitle != null)
                    tvTitle.text =
                        "Yakin ingin menyelesaikan pesanan ini?"
                btCancel.onClick {
                    dismiss()
                }

                btApply.setOnClickListener {
                    setUpdatePenjualan(item.id!!, "selesai")
                    dismiss()
                }

                show()
            }
        }


        btChatAdmin.onClick {
            var mPhoneNumber = "6285733331617"
            val mMessage = "Hello Admin Saya "+dataProfile.name+" mau mengonfirmasi pesanan saya "+item.id
            openWhatsApp(rootView, mPhoneNumber, mMessage)

        }
//        tvSalinAlamat.onClick {
//            myClipboard =
//                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//
//            myClip = ClipData.newPlainText("text", alamatlengkap)
//            myClipboard!!.setPrimaryClip(myClip!!)
//
//            Toast.makeText(this@RincianBelumBayarActivity, "Berhasil", Toast.LENGTH_SHORT).show();
//
//        }


        rvRincianProduk.setVerticalLayout(true)
        rvRincianProduk.adapter = rincianProdukAdapter
        rincianProdukAdapter.clearItems()
        item?.barang?.let {
            rincianProdukAdapter.addItems(
                it
            )
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
}