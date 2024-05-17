package com.asyabab.egj.ui.activity.pilihproduk

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.androidnetworking.AndroidNetworking
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.adapter.SliderItemAdapterExample
import com.asyabab.egj.data.models.detailbarang.DetailBarangResponse
import com.asyabab.egj.data.models.detailbarang.Gambar
import com.asyabab.egj.data.models.detailbarang.Stok
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.data.models.variasi.VariasiResponse
import com.asyabab.egj.ui.activity.CheckoutLangsungActivity
import com.asyabab.egj.ui.activity.MainActivity
import com.asyabab.egj.ui.activity.cart.DetailProdukPresenter
import com.asyabab.egj.ui.activity.tambahproduk.TambahProdukActivity
import com.asyabab.egj.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.gson.Gson
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_detailproduk.*
import kotlinx.android.synthetic.main.activity_detailproduk.btBack
import kotlinx.android.synthetic.main.activity_detailproduk.btCheckout
import kotlinx.android.synthetic.main.activity_detailproduk.btHapus
import kotlinx.android.synthetic.main.activity_detailproduk.framemain
import kotlinx.android.synthetic.main.activity_detailproduk.frameshimmer
import kotlinx.android.synthetic.main.activity_detailproduk.swipeResfresh
import kotlinx.android.synthetic.main.activity_detailproduk.tvKategori
import kotlinx.android.synthetic.main.activity_tambahproduk.*
import kotlinx.android.synthetic.main.popup_cancel.*
import kotlinx.android.synthetic.main.popup_pilihstok.*
import kotlinx.android.synthetic.main.popup_pilihstok.btTambah
import kotlinx.android.synthetic.main.rv_jumlahstok.view.*
import kotlinx.android.synthetic.main.rv_pilihstok.view.*
import kotlinx.android.synthetic.main.rv_pilihstok.view.btMin
import kotlinx.android.synthetic.main.rv_pilihstok.view.btPlus
import kotlinx.android.synthetic.main.rv_pilihstok.view.cbItem
import kotlinx.android.synthetic.main.rv_pilihstok.view.tvQty
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailProdukActivity : BaseActivity(), DetailProdukMVP.View {
    var item = ""
    var itemgambar = ""
    var title = ""
    var idbarang = ""
    var berat = ""
    private var presenter: DetailProdukPresenter? = null
    private var productItem = ArrayList<Datum>()
    var dataProfile = com.asyabab.egj.data.models.login.Datum()
    var dataProduk = com.asyabab.egj.data.models.barang.Datum()
    var url =
        "http://ichef.bbci.co.uk/onesport/cps/480/cpsprodpb/11136/production/_95324996_defoe_rex.jpg"
    var file: File? = null
    var dirPath: String? = ""
    var fileName: String? = ""

    private var mRequestPermissionHandler: RequestPermissionHandler? = null
    var detailItem = com.asyabab.egj.data.models.detailbarang.Datum()
    var sliderView: SliderView? = null
    var adapter: SliderItemAdapterExample? = null
    private var state: CollapsingToolbarLayoutState? = null
    lateinit var dialog: Dialog
    private enum class CollapsingToolbarLayoutState {
        EXPANDED, COLLAPSED, INTERNEDIATE
    }

    private var stokAdapter: RecyclerViewAdapter<Stok> = RecyclerViewAdapter(
        R.layout.rv_jumlahstok,
        onBind = { view, data, position ->
            view.tvStok.text = data.jumlahStok
            view.tvHarga.text = data.hargaStok
            view.tvUkuran.text = data.ukuranStok
            view.tvWarna.text = data.warnaStok

        })

    private var pilihStokAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_pilihstok
    ) { view, data, position ->

//        view.tvPilihStok.text = data.inventory
        view.tvPilihWarna.text=data.warna
        view.tvPilihUkuran.text=data.ukuran
        if(data.inventory=="0"){
            view.tvQty.setText("0")
        }
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
            Log.d("posisi", icounter.toString())
            view.btMin.isClickable = true

            if (icounter != null) {
                if (icounter < data.inventory!!.toInt()) {
                    icounter += 1
                    data.jumlah=icounter.toString()

                    Log.d("posisi+", icounter.toString())
//                    setUpdateJumlah(data.id.toString(), icounter.toString())
                    view.tvQty.setText(icounter.toString())
                } else {
                    view.btPlus.isClickable = false
                }
            }
        }
        view.btMin.onClick {
            Log.d("posisi", icounter.toString())
            view.btPlus.isClickable = true

            if (icounter != null) {
                if (icounter != 1) {
                    icounter -= 1
                    data.jumlah=icounter.toString()
                    Log.d("posisi-", icounter.toString())
//                    setUpdateJumlah(data.id.toString(), icounter.toString())
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

//        view.tvPilihHarga.text = data.harga?.toInt()?.convertRupiah()

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
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        val w: Window = window
//        w.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        setContentView(R.layout.activity_detailproduk)

        handleIntent(this.intent)
        val gson = Gson()

        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )
        try {
            if (item.isEmpty()){
                item = intent.getStringExtra("data").toString()
                itemgambar = intent.getStringExtra("gambar").toString()

                getItemDetail(item)
                getVariasi(item)
                Log.d("Berhasil", item)

            }
        } catch (e: Exception) {
            Log.d("Gagal", e.toString())

        }


        if (dataProfile.status=="Admin"){
            layouBttAdmin.visibility=View.VISIBLE
            layoutBtAnggota.visibility=View.INVISIBLE

            btHapus.onClick {
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
                            "Yakin ingin menghapus produk ini?"
                    btCancel.onClick {
                        dismiss()
                    }

                    btApply.setOnClickListener {
                        deleteBarang(item)
                        dismiss()
                    }

                    show()
                }
            }
            btUpdate.onClick {
                launchActivity<TambahProdukActivity> {
                    putExtra("data", item)
                }
            }
        }else{
            layouBttAdmin.visibility=View.INVISIBLE
            layoutBtAnggota.visibility=View.VISIBLE
        }



        presenter = DetailProdukPresenter(repository!!, this)
        presenter?.loadData()
        presenter?.getItemDetail(item)

        mRequestPermissionHandler = RequestPermissionHandler()
        Log.d("Percoba", item)

        rvStokA.setVerticalLayout(false)
        rvStokA.adapter = stokAdapter

        btCopy.setOnClickListener(View.OnClickListener {
            myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val text: String = tvDetailDeskripsi.text.toString()
            myClip = ClipData.newPlainText("text", text)
            myClipboard!!.setPrimaryClip(myClip!!)
            Toast.makeText(applicationContext, "Berhasil Disalin", Toast.LENGTH_SHORT).show()
        })
        btShare.onClick {
            Glide.with(this)
                .asBitmap()
                .load(itemgambar)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        mRequestPermissionHandler?.requestPermission(this@DetailProdukActivity,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            123,
                            object : RequestPermissionHandler.RequestPermissionListener {
                                override fun onSuccess() {
                                    Toast.makeText(applicationContext, "Bagikan Barang...", Toast.LENGTH_LONG).show()

                                    val text = "${tvDetailNama.text}\n\n" +
                                            "Harga\n" +
                                            "${Html.fromHtml(tvDetailHarga.text.toString())}\n\n" +
                                            "Deskripsi\n" +
                                            "${Html.fromHtml(tvDetailDeskripsi.text.toString())}\n\n" +
                                            "Aplikasi Endah Reseller Hijab\n" +
                                            "https://play.google.com/store/apps/details?id=com.asyabab.egj"

                                    val bytes = ByteArrayOutputStream()
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                    val path = MediaStore.Images.Media.insertImage(
                                        contentResolver,
                                        resource,
                                        UUID.randomUUID().toString() + ".png",
                                        "drawing"
                                    )
                                    val imageUri: Uri? = Uri.parse(path)
                                    val shareIntent = Intent()
                                    shareIntent.action = Intent.ACTION_SEND
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, text)
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                                    shareIntent.type = "image/jpg"
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(Intent.createChooser(shareIntent, "send"))
                                }

                                override fun onFailed() {
                                    Toast.makeText(
                                        this@DetailProdukActivity,
                                        "request permission failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
        }

        btTambahTasBelanja.onClick {
            dialog = Dialog(context)
            dialog.apply {
                setContentView(R.layout.popup_pilihstok)
                window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                rvVarianJumlah.setVerticalLayout(false)
                rvVarianJumlah.adapter = pilihStokAdapter

                btTambah.onClick {
                    if (productItem.size > 0) {
                        for (i in productItem!!.indices) {
                            setTambahKeranjang(dataProfile.id!!,productItem!![i].id!!,productItem!![i].jumlah!!)

                            var a=repository!!.getData("temp")

                            if(i== productItem!!.size-1){

                                if (a == "sukses"){
                                    Toast.makeText(
                                        this@DetailProdukActivity,
                                        "Berhasil Menambahkan",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    dialog.dismiss()

                                }else if(a=="gagal"){
                                    Toast.makeText(
                                        this@DetailProdukActivity,
                                        "Gagal Menambahkan",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }else{
                                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }



                    } else {
                        Toast.makeText(
                            this@DetailProdukActivity,
                            "Anda setidaknya harus memilih 1 produk",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                show()
            }
        }


        btCheckout.onClick {
            dialog = Dialog(context)
            dialog.apply {
                setContentView(R.layout.popup_pilihstok)
                window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                rvVarianJumlah.setVerticalLayout(false)
                rvVarianJumlah.adapter = pilihStokAdapter

                btTambah.onClick {

                    if (productItem.size > 0) {

//                        val productItems = ArrayList<Datum>()
//                        val productItem = Datum()
//                        productItem.id = detailItem.idBarang
//                        productItem.idbarang = detailItem.idBarang
////                productItem. = detailItem.data?.item?.id
//                        productItem.jumlah = qty.toString()
//                        productItems.add(productItem)
                        repository?.saveProduct(productItem)
                        launchActivity<CheckoutLangsungActivity>()

//                        for (i in productItem!!.indices) {
//                            setTambahKeranjang(dataProfile.id!!,productItem!![i].id!!,productItem!![i].jumlah!!)
//
//                            var a=repository!!.getData("temp")
//
//                            if(i== productItem!!.size-1){
//
//                                if (a == "sukses"){
//                                    Toast.makeText(
//                                        this@DetailProdukActivity,
//                                        "Berhasil Menambahkan",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                    dialog.dismiss()
//
//                                }else if(a=="gagal"){
//                                    Toast.makeText(
//                                        this@DetailProdukActivity,
//                                        "Gagal Menambahkan",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                }else{
//                                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
//                                        .show()
//                                }
//                            }
//                        }
                    } else {
                        Toast.makeText(
                            this@DetailProdukActivity,
                            "Anda setidaknya harus memilih 1 produk",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                show()
            }

//            var qty = 1
//            val btnsheet = layoutInflater.inflate(R.layout.popup_quantity, null)
//            val dialog = BottomSheetDialog(this@DetailProdukActivity)
//            dialog.setContentView(btnsheet)
//
//            dialog.btPlus.onClick {
//                var quantity = dialog.tvQty.text.toString().toInt()
//                quantity++
//                dialog.tvQty.setText(quantity.toString())
//                qty = quantity
//            }
//            dialog.btMin.onClick {
//                var quantity = dialog.tvQty.text.toString().toInt()
//                if (quantity > 1) {
//                    quantity--
//                    dialog.tvQty.setText(quantity.toString())
//                    qty = quantity
//                } else {
//
//                }
//            }
//            dialog.btApply.onClick {
//
//            }
//            dialog.btCancel.onClick {
//                dialog.dismiss()
//            }
//            dialog.show()
        }

//        btShare.onClick {
//            var url = "www.endoracare.com/product/$item"
////            var url="com.asyabab.endora/product?$item"
//
//            share(title, url)
//        }


        btBack.onClick {
            finish()
        }
        AndroidNetworking.initialize(applicationContext)

        //Folder Creating Into Phone Storage

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory().toString() + "/Image"

        fileName = "image.jpeg"

        //file Creating With Folder & Fle Name

        //file Creating With Folder & Fle Name
        file = File(dirPath, fileName)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val collapsingToolbarLayout =
            findViewById<View>(R.id.title) as TextView

        val appbar = findViewById<View>(R.id.appbar) as AppBarLayout
        appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (state !== CollapsingToolbarLayoutState.EXPANDED) {
                    collapsingToolbarLayout.text = "" //Set title to EXPANDED
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                if (state !== CollapsingToolbarLayoutState.COLLAPSED) {
                    collapsingToolbarLayout.text = title
                    state =
                        CollapsingToolbarLayoutState.COLLAPSED //Modified status marked as folded
                }
            } else {
                if (state !== CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (state === CollapsingToolbarLayoutState.COLLAPSED) {
//                        playButton.setVisibility(View.GONE) //Hide Play Button When Changed from Folding to Intermediate State
                    }
                    state =
                        CollapsingToolbarLayoutState.INTERNEDIATE //Modify the status tag to the middle
                }
            }
        })
        sliderView = findViewById(R.id.imageSlider)

        swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getItemDetail(item)
            swipeResfresh.isRefreshing = false;

        })

        adapter = SliderItemAdapterExample(this)
        sliderView!!.setSliderAdapter(adapter!!)
        sliderView!!.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView!!.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView!!.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        sliderView!!.indicatorSelectedColor = Color.WHITE
        sliderView!!.indicatorUnselectedColor = Color.GRAY

        sliderView!!.setOnIndicatorClickListener {
            Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()

            Log.i(
                "GGG",
                "onIndicatorClicked: " + sliderView!!.currentPagePosition
            )
        }

//        renewItems()
    }


    private fun handleIntent(intent: Intent?) {
        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data
        showDeepLinkOffer(appLinkAction, appLinkData)
    }

    private fun showDeepLinkOffer(appLinkAction: String?, appLinkData: Uri?) {
        // 1
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null) {
            // 2
            item = appLinkData.getQueryParameter("i").toString()
            if (item.isNullOrBlank().not()) {
                getItemDetail(item)
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onRestart() {
        super.onRestart()

//        cekOngkir("151", repository!!.getLokasi().toString(), berat, "jne", "REG")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mRequestPermissionHandler!!.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
    }
    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }
    fun share(judul: String, text: String) {
        val intent= Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, judul);
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
    }

    fun getItemDetail(id: String) {
        repository!!.getDetailItem(
            id,
            object : DetailBarangResponse.DetailBarangResponseCallback {
                override fun onSuccess(detailBarangResponse: DetailBarangResponse) {
                    if (detailBarangResponse.status == true) {
                        detailItem = detailBarangResponse.data?.get(0)!!

                        adapter!!.renewItems((detailBarangResponse.data?.get(0)?.gambar as MutableList<Gambar>?)!!)

                        stokAdapter.clearItems()
                        detailBarangResponse.data?.get(0)?.stok?.let { stokAdapter.addItems(it) }

                        tvDetailDeskripsi.text =
                            detailBarangResponse.data?.get(0)?.keteranganBarang.toString()
//                        tvKategori.text = detailItemResponse.data?.item?.categories?.get(0)?.name.toString()
                        title = detailBarangResponse.data?.get(0)?.namaBarang.toString()
                        tvDetailNama.text = title
                        tvDetailHarga.text= detailBarangResponse.data?.get(0)?.hargaStok.toString()
                        tvKategori.text =
                            detailBarangResponse.data?.get(0)?.kategoriBarang.toString()

                        framemain.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@DetailProdukActivity,
                        "Server Sedang Error$message",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    fun getVariasi(id: String) {
        repository!!.getVariasi(
            id,
            object : VariasiResponse.VariasiResponseCallback {
                override fun onSuccess(variasiResponse: VariasiResponse) {
                    if (variasiResponse.status == true) {
                        repository!!.getWarna().clear()
                        repository!!.getUkuran().clear()
                        variasiResponse.data?.get(0)?.warna?.toCollection(repository!!.getWarna())
                        variasiResponse.data?.get(0)?.ukuran?.toCollection(repository!!.getUkuran())

                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(
                        this@DetailProdukActivity,
                        "Server Sedang Error$message",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    fun deleteBarang(itemId: String) {
        repository!!.deleteBarang(
            itemId,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        Toast.makeText(applicationContext, "Berhasil", Toast.LENGTH_LONG)
                            .show()
                        launchActivityWithNewTask<MainActivity>()
                    }
                }

                override fun onFailure(message: String) {
                    Log.d("onFailure", message)

                    Toast.makeText(applicationContext, "Server Sedang Error", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun setTambahKeranjang( iduser: String,
                            idstok: String,
                            qty: String) :String{
        var temp=""
        repository!!.setTambahKeranjang(
            iduser, idstok, qty,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        loading?.hide()

                        repository?.saveData("temp","sukses")
                        repository!!.getData("temp")?.let { Log.d("UbahProfil", it) }


                    } else {
                        loading?.hide()
                        repository?.saveData("temp","gagal")


                    }
                }

                override fun onFailure(message: String) {
                    repository?.saveData("temp","error")

                    Log.d("UbahProfil", "signInsuccess" + message)

                }

            })
        Log.d("UbahProfil", temp)

        return temp
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
        pilihStokAdapter.clearItems()
        pilihStokAdapter.addItems(productItems.data!!)
    }

    override fun updateTotalItem(productItems: ArrayList<Datum>) {
        productItem = productItems
        Log.d("cook2", productItem.size.toString())
    }

    override fun updateTotalPrice(value: Int) {
//        tv_SubTotal.text = value.convertRupiah()
    }
}
