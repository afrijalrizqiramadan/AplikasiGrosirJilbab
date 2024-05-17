package com.asyabab.egj.ui.activity.tambahproduk

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.detailbarang.DetailBarangResponse
import com.asyabab.egj.data.models.gambar.Datum
import com.asyabab.egj.data.models.gambar.GambarResponse
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.data.models.localstok
import com.asyabab.egj.ui.activity.DaftarActivity
import com.asyabab.egj.ui.activity.MainActivity
import com.asyabab.egj.ui.activity.cart.DetailProdukPresenter
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukMVP
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_detailproduk.*
import kotlinx.android.synthetic.main.activity_tambahproduk.*
import kotlinx.android.synthetic.main.activity_tambahproduk.tvKategori
import kotlinx.android.synthetic.main.rv_gambar.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException


class TambahProdukActivity : BaseActivity(), DetailProdukMVP.View, AdapterView.OnItemSelectedListener {
    val REQUEST_IMAGE = 100
    var random1 =""
    var item = ""
    var detailItem = com.asyabab.egj.data.models.detailbarang.Datum()

    private var presenter: DetailProdukPresenter? = null
    private var productItem = java.util.ArrayList<com.asyabab.egj.data.models.keranjang.Datum>()
    private val TAG: String = DaftarActivity::class.java.simpleName
    var dataProfile = com.asyabab.egj.data.models.login.Datum()

    private var imageAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_gambar,
        onBind = { view, data, position ->

            var url="https://api.endahgrosirjilbab.com/storage/barang/"+data.namaGambar
            view.tvGambar.loadImageFromResources(this, url)

            view.tvDelete.onClick {
                deleteGambar(data.namaGambar!!,data.idBarang!!)
            }
        })

    private var stokItems: ArrayList<localstok>? = null
    private var gambarItems: ArrayList<Datum>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahproduk)
        stokItems = repository?.getStok()

        try {
            if (item.isEmpty()){
                item = intent.getStringExtra("data").toString()
                if (item!="null"){
                    getItemDetail(item)
                    Log.d("Berhasil", item)
                    btEdit.visibility=View.VISIBLE
                    btTambah.visibility=View.GONE
                }else{
                    random1 = (0..1000000).shuffled().last().toString()
                    repository?.saveData("tokenbarang", random1)
                    Log.e("hasil", random1.toString())
                    btEdit.visibility=View.GONE
                    repository!!.getWarna().clear()
                    repository!!.getUkuran().clear()
                    repository!!.getStok().clear()
                    btTambah.visibility=View.VISIBLE
                }

            }
        } catch (e: Exception) {
            Log.d("Gagal", e.toString())
            random1 = (0..1000000).shuffled().last().toString()
            repository?.saveData("tokenbarang", random1)
            Log.e("hasil", random1.toString())
            btEdit.visibility=View.GONE
            repository!!.getWarna().clear()
            repository!!.getUkuran().clear()
            repository!!.getStok().clear()
            btTambah.visibility=View.VISIBLE
        }
        val gson = Gson()

        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )

        rvGambarProduk.setHorizontalLayout(false)
        rvGambarProduk.adapter = imageAdapter

        btEdit.onClick {
            val jsonObject = JSONObject()
            try {
                val jsonArray = JSONArray()

                for (i in stokItems!!.indices) {
                    val jsonObj = JSONObject()
                    jsonObj.put("idstok", stokItems!![i].id)
                    jsonObj.put("warnastok", stokItems!![i].warna)
                    jsonObj.put("ukuranstok", stokItems!![i].ukuran)
                    jsonObj.put("hargastok", stokItems!![i].harga)
                    jsonObj.put("jumlahstok", stokItems!![i].stok)

                    jsonArray.put(jsonObj)
                }


                jsonObject.put("item", jsonArray)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.e("cobajson", jsonObject.toString())
//            for (i in imageAdapter!!.items.indices) {
//                Log.e("cobagambar", imageAdapter!!.items.get(i).idGambar.toString())
//                Log.e("cobagambar", random1.toString())
//
////                gambarItems!![i].idGambar?.let { updateGambar(it,random1.toString()) }
//            }
            if (inputNama.text.toString()==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Nama Produk",
                    Toast.LENGTH_LONG
                ).show()
            }else if(inputRincian.text.toString()==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Rincian",
                    Toast.LENGTH_LONG
                ).show()
            }else if(repository!!.getData("kategori")==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Rincian",
                    Toast.LENGTH_LONG
                ).show()
            }else if(imageAdapter.itemCount<1){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Gambar Dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.warna ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Warna Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.warna ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Warna Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.ukuran ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Ukuran Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                setUpdateBarang(repository?.getData("tokenbarang")!!, inputNama.text.toString(), inputRincian.text.toString(), repository!!.getData("kategori")!!,jsonObject.toString())
//                setUpdateBarang("247818", "produkoaas", "Okoa", "1","{\"item\":[{\"idstok\":\"607\",\"warnastok\":\"biru\",\"ukuranstok\":\"L\",\"hargastok\":\"5000\",\"jumlahstok\":\"4\"},{\"idstok\":\"605\",\"warnastok\":\"kuning\",\"ukuranstok\":\"L\",\"hargastok\":\"25000\",\"jumlahstok\":\"91\"}]}")

            }
        }
        btTambahGambar.onClick {
            onProfileImageClick()
        }
        inputKategori.onClick {
            launchActivity<TambahKategoriActivity> {  }
        }
        inputVarian.onClick {
            launchActivity<TambahVariasiActivity> {
                putExtra("varian",detailItem)
            }
        }
        inputStokHarga.onClick {
            launchActivity<TambahHargaStokActivity> {
                putExtra("stok",detailItem)
            }
        }
        btTambah.onClick {
            val jsonObject = JSONObject()
            try {
                val jsonArray = JSONArray()

                for (i in stokItems!!.indices) {
                    val jsonObj = JSONObject()
                    jsonObj.put("idstok", stokItems!![i].id)
                    jsonObj.put("warnastok", stokItems!![i].warna)
                    jsonObj.put("ukuranstok", stokItems!![i].ukuran)
                    jsonObj.put("hargastok", stokItems!![i].harga)
                    jsonObj.put("jumlahstok", stokItems!![i].stok)

                    jsonArray.put(jsonObj)
                }


                jsonObject.put("item", jsonArray)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.e("cobajson", jsonObject.toString())
//            for (i in imageAdapter!!.items.indices) {
//                Log.e("cobagambar", imageAdapter!!.items.get(i).idGambar.toString())
//                Log.e("cobagambar", random1.toString())
//
////                gambarItems!![i].idGambar?.let { updateGambar(it,random1.toString()) }
//            }
            if (inputNama.text.toString()==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Nama Produk",
                    Toast.LENGTH_LONG
                ).show()
            }else if(inputRincian.text.toString()==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Rincian",
                    Toast.LENGTH_LONG
                ).show()
            }else if(repository!!.getData("kategori")==""){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Rincian",
                    Toast.LENGTH_LONG
                ).show()
            }else if(imageAdapter.itemCount<1){
                Toast.makeText(
                    applicationContext,
                    "Masukkan Gambar Dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.warna ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Warna Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.warna ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Warna Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }else if(stokItems?.get(0)?.ukuran ==null){
                Toast.makeText(
                    applicationContext,
                    "Varian Ukuran Kosong",
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                setTambahBarang(repository?.getData("tokenbarang")!!, inputNama.text.toString(), inputRincian.text.toString(), repository!!.getData("kategori")!!,jsonObject.toString())

            }
        }

    }
    lateinit var uri:Uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data?.getParcelableExtra<Uri>("path")!!
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    setGambar(repository?.getData("tokenbarang")!!)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        tvKategori.text = repository!!.getData("namakategori")
    }
    var listgambar: ArrayList<Datum> = ArrayList()

    fun getItemDetail(id: String) {
        repository!!.getDetailItem(
            id,
            object : DetailBarangResponse.DetailBarangResponseCallback {
                override fun onSuccess(detailBarangResponse: DetailBarangResponse) {
                    if (detailBarangResponse.status == true) {
                        detailItem = detailBarangResponse.data?.get(0)!!
                        repository?.saveData("tokenbarang", detailItem.idBarang!!)

//                        adapter!!.renewItems((detailBarangResponse.data?.get(0)?.gambar as MutableList<Gambar>?)!!)
//
//                        stokAdapter.clearItems()
//                        detailBarangResponse.data?.get(0)?.stok?.let { stokAdapter.addItems(it) }
                        imageAdapter.clearItems()

                        for (i in detailBarangResponse?.data?.get(0)?.gambar!!.indices) {
                            val jsonObj = Datum()
                            jsonObj.idGambar= detailBarangResponse?.data?.get(0)?.gambar!![i].idGambar
                            jsonObj.namaGambar=detailBarangResponse?.data?.get(0)?.gambar!![i].namaGambar
                            jsonObj.idBarang=detailBarangResponse?.data?.get(0)?.idBarang
                            jsonObj.idGambar?.let { Log.d("Login", it) }

                            listgambar.add(jsonObj)
                        }
                        imageAdapter.addItems(listgambar)

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
                        repository!!.saveData("kategori",detailBarangResponse.data?.get(0)?.idkategoriBarang.toString())

                        tvKategori.text = detailBarangResponse.data?.get(0)?.kategoriBarang.toString()
                        inputRincian.setText(detailBarangResponse.data?.get(0)?.keteranganBarang.toString())
                        title = detailBarangResponse.data?.get(0)?.namaBarang.toString()
                        inputNama.setText(title)
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@TambahProdukActivity,
                        "Server Sedang Error$message",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }
    fun setTambahBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String) {
        repository?.setTambahBarang(
            idbarang,
            namabarang,
            keteranganbarang,
            idkategori,
            produk,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_LONG)
                            .show()
                        launchActivityWithNewTask<MainActivity>()
                        repository!!.getWarna().clear()
                        repository!!.getUkuran().clear()
                        repository!!.getStok().clear()
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

    fun setUpdateBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String) {
        repository?.setUpdateBarang(
            idbarang,
            namabarang,
            keteranganbarang,
            idkategori,
            produk,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_LONG)
                            .show()
                        launchActivityWithNewTask<MainActivity>()
                        repository!!.getWarna().clear()
                        repository!!.getUkuran().clear()
                        repository!!.getStok().clear()
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


    fun deleteGambar(
        namagambar: String,
        idbarang: String
    ) {
        repository?.deleteGambar(
            namagambar,idbarang,
            object : GambarResponse.GambarResponseCallback {
                override fun onSuccess(gambarResponse: GambarResponse) {
                    if (gambarResponse.status == true) {
                        imageAdapter.clearItems()
                        gambarResponse?.data?.let { imageAdapter.addItems(it) }

//                        imageAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(applicationContext, "Gagal Mengambil Data", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(message: String) {
                    imageAdapter.clearItems()

//                    Log.e(TAG, "respon register $message")
//                    Toast.makeText(
//                        applicationContext,
//                        "Server Sedang Error",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }
        )
    }

    fun setGambar(id:String) {
        repository?.tambahGambar(
            File(uri.path),
            id,
            object : GambarResponse.GambarResponseCallback {
                override fun onSuccess(gambarResponse: GambarResponse) {
                    loading?.dismiss()
                    if (gambarResponse.status == true) {
                        imageAdapter.clearItems()
                        gambarResponse?.data?.let { imageAdapter.addItems(it) }

                        gambarResponse?.data?.let { gambarItems?.addAll(it)}

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

    fun updateGambar(idgambar:String, idbarang:String) {
        repository?.updateGambar(
            idgambar,
            idbarang,
            object : GambarResponse.GambarResponseCallback {
                override fun onSuccess(gambarResponse: GambarResponse) {
                    loading?.dismiss()
                    if (gambarResponse.status == true) {
                        Toast.makeText(applicationContext, "Sukses", Toast.LENGTH_LONG)
                            .show()
//                        imageAdapter.clearItems()
//                        gambarResponse?.data?.let { imageAdapter.addItems(it) }

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
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@TambahProdukActivity)
        builder.setTitle("Izin Perangkat")
        builder.setMessage("Izin Perangkat")
        builder.setPositiveButton("Pergi ke Pengaturan") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }
    private fun onProfileImageClick() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }
    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }
    private fun launchCameraIntent() {
        val intent = Intent(this@TambahProdukActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@TambahProdukActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }
    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading!!.show()
        } else {
            loading!!.dismiss()
        }    }

    override fun showError(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            .show()    }

    override fun upDateList(productItems: KeranjangResponse) {
//        pilihStokAdapter.clearItems()
//        pilihStokAdapter.addItems(productItems.data!!)
    }

    override fun updateTotalItem(productItems: java.util.ArrayList<com.asyabab.egj.data.models.keranjang.Datum>) {
        productItem = productItems
        Log.d("cook2", productItem.size.toString())
    }

    override fun updateTotalPrice(value: Int) {
//        tv_SubTotal.text = value.convertRupiah()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}