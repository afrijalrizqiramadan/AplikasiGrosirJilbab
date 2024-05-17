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
import com.asyabab.egj.data.models.penjualan.Barang
import com.asyabab.egj.data.models.penjualan.Prose
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rincianbelumbayar.*
import kotlinx.android.synthetic.main.popup_cancel.*
import kotlinx.android.synthetic.main.rv_rincianproduk.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat


class RincianBelumBayarActivity : BaseActivity() {
    var item = Prose()
    var id = ""
    private var rincianProdukAdapter: RecyclerViewAdapter<Barang> =
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
    private var stokItems: List<Barang>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rincianbelumbayar)

        item = intent.getSerializableExtra("data") as Prose
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
            btBatalkanPesanan.visibility=View.VISIBLE
            btKonfirmasi.visibility=View.VISIBLE
            btLakukanPembayaran.visibility=View.GONE
            layoutPembeli.visibility=View.VISIBLE
            tvAlamatPembeli.text=item.user?.get(0)?.alamat
            tvNomerPembeli.text=item.user?.get(0)?.nohp
            tvNamaPembeli.text=item.user?.get(0)?.nama
        }else{
            btBatalkanPesanan.visibility=View.GONE
            btLakukanPembayaran.visibility=View.VISIBLE
            btKonfirmasi.visibility=View.GONE
            layoutPembeli.visibility=View.GONE

        }
        stokItems = item.barang

        btKonfirmasi.onClick {
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
                        "Yakin ingin mengirim pesanan ini?"
                btCancel.onClick {
                    dismiss()
                }

                btApply.setOnClickListener {
                    setUpdatePenjualan(item.id!!, "kirim")
                    dismiss()
                }

                show()
            }
        }

        btBatalkanPesanan.onClick {
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
                        "Yakin ingin membatalkan pesanan ini?"
                btCancel.onClick {
                    dismiss()
                }

                btApply.setOnClickListener {

                    val jsonObject = JSONObject()
                    try {
                        val jsonArray = JSONArray()

                        for (i in stokItems!!.indices) {
                            val jsonObj = JSONObject()
                            jsonObj.put("jumlahstok", stokItems!![i].jumlahPenjualan)
                            jsonObj.put("idstok", stokItems!![i].idStok)

                            jsonArray.put(jsonObj)
                        }


                        jsonObject.put("item", jsonArray)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.e("cobajson", jsonObject.toString())
                    setUpdateStok(item.id!!, jsonObject.toString())
                    dismiss()
                }

                show()
            }
        }


        btLakukanPembayaran.onClick {
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

    fun setUpdateStok(id: String,
        produk: String)
    {
        repository?.setUpdateStok(
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
//    fun openWhatsApp(view: View?) {
//        val pm = packageManager
//        try {
//            val waIntent = Intent(Intent.ACTION_SEND)
//            waIntent.type = "text/plain"
//            val text = "This is  a Test" // Replace with your own message.
//            val info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
//            //Check if package exists or not. If not then code
//            //in catch block will be called
//            waIntent.setPackage("com.whatsapp")
//            waIntent.putExtra(Intent.EXTRA_TEXT, text)
//            startActivity(Intent.createChooser(waIntent, "Share with"))
//        } catch (e: PackageManager.NameNotFoundException) {
//            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
//                .show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}