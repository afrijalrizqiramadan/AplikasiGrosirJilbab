package com.asyabab.egj.data


import android.content.Context
import android.content.Intent
import com.asyabab.egj.data.local.SharedPrefHelper
import com.asyabab.egj.data.models.barang.BarangResponse

import com.asyabab.egj.data.models.checkout.CheckoutResponse
import com.asyabab.egj.data.models.detailbarang.DetailBarangResponse
import com.asyabab.egj.data.models.gambar.GambarResponse
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.egj.data.models.keranjang.Datum
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.data.models.localstok
import com.asyabab.egj.data.models.login.LoginResponse
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.data.models.register.RegisterResponse
import com.asyabab.egj.data.models.variasi.VariasiResponse
import com.asyabab.egj.data.network.APIRequest
import com.asyabab.egj.ui.activity.LoginActivity
import java.io.File
import java.util.*

class Repository(private val mContext: Context) {

    private val apiRequest: APIRequest = APIRequest()
    private val prefs: SharedPrefHelper = SharedPrefHelper(mContext)

    private var product = ArrayList<Datum>()
    private var listwarna: ArrayList<String> = ArrayList()
    private var listukuran: ArrayList<String> = ArrayList()
    private var liststok: ArrayList<localstok> = ArrayList()


    fun saveProduct(productItem: ArrayList<Datum>) {
        product = productItem
    }

    fun getProduvt(): ArrayList<Datum> {
        return product
    }

    fun saveWarna(listwarnaItem: ArrayList<String>) {
        listwarna = listwarna
    }

    fun getWarna(): ArrayList<String> {
        return listwarna
    }

    fun saveUkuran(listukuranItem: ArrayList<String>) {
        listukuran = listukuranItem
    }

    fun getUkuran(): ArrayList<String> {
        return listukuran
    }

    fun saveStok(pliststokItem: ArrayList<localstok>) {
        liststok = pliststokItem
    }

    fun getStok(): ArrayList<localstok> {
        return liststok
    }

    fun login(
        username: String,
        password: String,
        callback: LoginResponse.LoginResponseCallback
    ) {
        apiRequest.login(username, password, callback)
    }
    fun deleteUser(
        id: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.deleteUser(id, callback)
    }



    fun getAnggota(
        callback: LoginResponse.LoginResponseCallback
    ) {
        apiRequest.getAnggota( callback)
    }
    fun setBarang(
        callback: BarangResponse.BarangResponseCallback
    ) {
        apiRequest.barang(callback)
    }
    fun filterbarang(
        nama: String,
        callback: BarangResponse.BarangResponseCallback
    ) {
        apiRequest.filterbarang(nama,callback)
    }


    fun logoutSession() {
        prefs.clear()
        val i = Intent(mContext, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        mContext.startActivity(i)
    }
    fun setLogin(islogin: Boolean) {
        prefs.setLogin(islogin)
    }
    fun cekLogin() : Boolean {
        return prefs.cekLogin()
    }


    fun getPembelian(id: String, callback: PenjualanResponse.PenjualanResponseCallback) {
        apiRequest.getPembelian(id, callback)
    }
    fun getPembelianAll(callback: PenjualanResponse.PenjualanResponseCallback) {
        apiRequest.getPembelianAll(callback)
    }


    fun getListKategori(callback: ListKategoriResponse.ListKategoriResponseCallback) {
        apiRequest.getListKategori(callback)
    }


    fun getDetailItem(
        id: String,
        callback: DetailBarangResponse.DetailBarangResponseCallback
    ) {
        apiRequest.getDetailItem(id, callback)
    }

    fun getVariasi(
        id: String,
        callback: VariasiResponse.VariasiResponseCallback
    ) {
        apiRequest.getVariasi(id, callback)
    }
    fun tambahKategori(
        nama: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.tambahKategori(nama, callback)
    }
    fun hapusKategori(
        nama: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.hapusKategori(nama, callback)
    }
    fun getKeranjang(
        id: String,
        callback: KeranjangResponse.KeranjangResponseCallback
    ) {
        apiRequest.getKeranjang(id, callback)
    }

    fun deleteKeranjang(
        itemId: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.deleteKeranjang(itemId, callback)
    }
    fun deleteBarang(
        itemId: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.deleteBarang(itemId, callback)
    }

    fun register(
        nama: String,
        alamat: String,
        nohp: String,
        username: String,
        password: String,
        status: String,
        img: File,
        callback: RegisterResponse.RegisterResponseCallback
    ) {
        apiRequest.register(
            nama,
            alamat,
            nohp,
            username,
            password,
            status,
            img,
            callback
        )
    }
    fun tambahGambar(
        img: File,
        id: String,

        callback: GambarResponse.GambarResponseCallback
    ) {
        apiRequest.tambahGambar(
            img,
            id,
            callback
        )
    }
    fun deleteGambar(
        namagambar: String,
        idbarang: String,
        callback: GambarResponse.GambarResponseCallback
    ) {
        apiRequest.deleteGambar(namagambar, idbarang, callback)
    }
    fun updateGambar(
        idgambar: String,
        idbarang: String,
        callback: GambarResponse.GambarResponseCallback
    ) {
        apiRequest.updateGambar(
            idgambar,
            idbarang,
            callback
        )
    }
    fun updateUser(
        id: String,
        nama: String,
        alamat: String,
        nohp: String,
        username: String,
        password: String,
        callback: RegisterResponse.RegisterResponseCallback
    ) {
        apiRequest.updateUser(
            id,
            nama,
            alamat,
            nohp,
            username,
            password,
            callback
        )
    }

    fun getToken(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_TOKEN)
    }

    fun saveToken(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_TOKEN, token!!)
    }

    fun getLokasi(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_LOKASI)
    }

    fun saveLokasiKeterangan(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_LOKASIKETERANGAN, token!!)
    }

    fun getLokasiKeterangan(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_LOKASIKETERANGAN)
    }

    fun saveLokasi(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_LOKASI, token!!)
    }
//
//    fun savePembelian(data: com.asyabab.endora.data.models.payment.getpembelian.Data) {
//        prefs.putPembelian(SharedPrefHelper.ACCES_PEMBELIAN, data!!)
//    }

    fun getPembelian(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_PEMBELIAN)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        prefs.setFirstTimeLaunch(isFirstTime)
    }

    fun setTambahKeranjang(
        iduser: String,
        idstok: String,
        qty: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setTambahKeranjang(iduser, idstok, qty, callback)
    }
    fun setTambahBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setTambahBarang(idbarang,namabarang, keteranganbarang, idkategori, produk, callback)
    }
    fun setUpdateBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdateBarang(idbarang,namabarang, keteranganbarang, idkategori, produk, callback)
    }

    fun setUpdateStok(
        id: String,
        stok: String,
    callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdateStok(id,stok,callback)
    }
    fun setDeletePenjualan(
        id: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setDeletePenjualan(id,callback)
    }
    fun setUpdatePenjualan(
        id: String,
        status: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdatePenjualan(id,status,callback)
    }
    fun getAdmin(
        callback: LoginResponse.LoginResponseCallback
    ) {
        apiRequest.getAdmin( callback)
    }
    fun setUpdateJumlah(
        id: String,
        qty: String,

        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdateJumlah(id, qty, callback)
    }

    fun setUpdateStatus(
        id: String,
        status: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        apiRequest.setUpdateJumlah(id, status, callback)
    }

    fun saveProfile(token: String?) {
        prefs.putString(SharedPrefHelper.ACCES_PROFILE, token!!)
    }

    fun saveData(param: String, isi: String?) {
        prefs.putString(param, isi!!)
    }

    fun getData(param: String): String? {
        return prefs.getString(param)
    }


    fun getProfile(): String? {
        return prefs.getString(SharedPrefHelper.ACCES_PROFILE)
    }


    fun checkout(
        idpenjualan: String,
        iduser: String,
        total: String,
        catatan: String,
        pembayaran: String,
        produk: String,
        callback: CheckoutResponse.CheckoutResponseCallback
    ) {
        apiRequest.checkout(idpenjualan, iduser, total, catatan, pembayaran, produk, callback)
    }



    private var mCheckout = CheckoutResponse()
    fun saveCheckoutResponse(checkoutResponse: CheckoutResponse) {
        mCheckout = checkoutResponse
    }

    fun getCheckoutResponse(): CheckoutResponse {
        return mCheckout
    }

}