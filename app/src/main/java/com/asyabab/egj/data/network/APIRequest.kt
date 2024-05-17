package com.asyabab.egj.data.network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.asyabab.egj.data.models.barang.BarangResponse
import com.asyabab.egj.data.models.checkout.CheckoutResponse
import com.asyabab.egj.data.models.detailbarang.DetailBarangResponse
import com.asyabab.egj.data.models.gambar.GambarResponse
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.egj.data.models.keranjang.KeranjangResponse
import com.asyabab.egj.data.models.login.LoginResponse
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.data.models.register.RegisterResponse
import com.asyabab.egj.data.models.variasi.VariasiResponse
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.io.File

class APIRequest {

    private val BASE_URL = "https://api.endahgrosirjilbab.com/"

    private val TAG = "hasil"
    private val gson = GsonBuilder().create()
    private var requestUrl = ""

    fun login(
        username: String,
        password: String,
        callback: LoginResponse.LoginResponseCallback
    ) {
        requestUrl = BASE_URL + "auth/login"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                        val response1 =
                            gson.fromJson(response.toString(), LoginResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")
                        }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Login Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }

    fun deleteUser(
        id: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "user/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Login Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }

    fun deleteGambar(
        namagambar: String,
        idbarang: String,
        callback: GambarResponse.GambarResponseCallback
    ) {
        requestUrl = BASE_URL + "gambar/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("nama_gambar", namagambar)
            .addBodyParameter("idbarang", idbarang)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GambarResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Login Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }

    fun getAnggota(
        callback: LoginResponse.LoginResponseCallback
    ) {
        requestUrl = BASE_URL + "user"

        AndroidNetworking.get(requestUrl)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), LoginResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Login Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }
    fun barang(
        callback: BarangResponse.BarangResponseCallback
    ) {
        requestUrl = BASE_URL + "barang"

        AndroidNetworking.get(requestUrl)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), BarangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }
    fun filterbarang(
        nama: String,
        callback: BarangResponse.BarangResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/search"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", nama)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), BarangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
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
        requestUrl = BASE_URL + "auth/register"

        AndroidNetworking.upload(requestUrl)
            .addMultipartParameter("nama", nama)
            .addMultipartParameter("alamat", alamat)
            .addMultipartParameter("nohp", nohp)
            .addMultipartParameter("username", username)
            .addMultipartParameter("password", password)
            .addMultipartParameter("status", status)
            .addMultipartFile("img", img)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    val response1 =
                        gson.fromJson(response.toString(), RegisterResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }
    fun tambahGambar(
        img: File,
        id: String,
        callback: GambarResponse.GambarResponseCallback
    ) {
        requestUrl = BASE_URL + "gambar/create"

        AndroidNetworking.upload(requestUrl)
            .addMultipartFile("img", img)
            .addMultipartParameter("id_barang", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    val response1 =
                        gson.fromJson(response.toString(), GambarResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }
    fun updateGambar(
        idgambar: String,
        idbarang: String,
        callback: GambarResponse.GambarResponseCallback
    ) {
        requestUrl = BASE_URL + "gambar/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idgambar", idgambar)
            .addBodyParameter("idbarang", idbarang)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    val response1 =
                        gson.fromJson(response.toString(), GambarResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
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
        requestUrl = BASE_URL + "user/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("nama", nama)
            .addBodyParameter("alamat", alamat)
            .addBodyParameter("nohp", nohp)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .addBodyParameter("id", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    val response1 =
                        gson.fromJson(response.toString(), RegisterResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }

    fun setTambahKeranjang(
        iduser: String,
        idstok: String,
        qty: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "keranjang/create"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("iduser", iduser)
            .addBodyParameter("idstok", idstok)
            .addBodyParameter("jumlahkeranjang", qty)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun setTambahBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/create"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("namabarang", namabarang)
            .addBodyParameter("idbarang", idbarang)
            .addBodyParameter("keteranganbarang", keteranganbarang)
            .addBodyParameter("idkategori", idkategori)
            .addBodyParameter("produk", produk)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }

    fun setUpdateBarang(
        idbarang: String,
        namabarang: String,
        keteranganbarang: String,
        idkategori: String,
        produk: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("namabarang", namabarang)
            .addBodyParameter("idbarang", idbarang)
            .addBodyParameter("keteranganbarang", keteranganbarang)
            .addBodyParameter("idkategori", idkategori)
            .addBodyParameter("produk", produk)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun setUpdateStok(
        id: String,
        stok: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/updatestok"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("produk", stok)
            .addBodyParameter("idpenjualan", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun setDeletePenjualan(
        id: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "penjualan/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idpenjualan", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun getAdmin(
        callback: LoginResponse.LoginResponseCallback
    ) {
        requestUrl = BASE_URL + "admin"

        AndroidNetworking.get(requestUrl)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), LoginResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure("Login Gagal")
                    Log.e(TAG, "respon login ${anError.errorBody}")
                }
            })
    }
    fun setUpdatePenjualan(
        id: String,
        status: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "penjualan/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("status", status)
            .addBodyParameter("idpenjualan", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun setUpdateJumlah(
        id: String,
        qty: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "keranjang/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", id)
            .addBodyParameter("jumlah", qty)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun setUpdateStatus(
        id: String,
        status: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "keranjang/update"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idpenjulan", id)
            .addBodyParameter("status", status)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun getPembelian(
        id: String,
        callback: PenjualanResponse.PenjualanResponseCallback
    ) {
        requestUrl = BASE_URL + "penjualan"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("user", id)

            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    if (response != null) {
                        val response1 =
                            gson.fromJson(response.toString(), PenjualanResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")

                        }
                    } else {
                        callback.onFailure("Cannot get Object")
                        Log.e(TAG, "onResrr: $response")

                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun getPembelianAll(
        callback: PenjualanResponse.PenjualanResponseCallback
    ) {
        requestUrl = BASE_URL + "penjualan/penjualanall"

        AndroidNetworking.post(requestUrl)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")
                    if (response != null) {
                        val response1 =
                            gson.fromJson(response.toString(), PenjualanResponse::class.java)
                        if (response1 != null) {
                            callback.onSuccess(response1)
                        } else {
                            callback.onFailure("Cannot get Object")

                        }
                    } else {
                        callback.onFailure("Cannot get Object")
                        Log.e(TAG, "onResrr: $response")

                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }


    fun getKeranjang(
        id: String,
        callback: KeranjangResponse.KeranjangResponseCallback
    ) {
        requestUrl = BASE_URL + "keranjang/search"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter(
                "id", id
            )
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), KeranjangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }

    fun deleteKeranjang(
        itemId: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "keranjang/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id_keranjang", itemId)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun deleteBarang(
        itemId: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id", itemId)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 = gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                }
            })
    }
    fun getListKategori(
        callback: ListKategoriResponse.ListKategoriResponseCallback
    ) {
        requestUrl = BASE_URL + "kategori"

        AndroidNetworking.get(requestUrl)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), ListKategoriResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }
    fun tambahKategori(
        nama: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "kategori/create"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("nama", nama)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }
    fun hapusKategori(
        nama: String,
        callback: GeneralResponse.GeneralResponseCallback
    ) {
        requestUrl = BASE_URL + "kategori/delete"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("id_kategori", nama)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), GeneralResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }


    fun getDetailItem(
        id: String,
        callback: DetailBarangResponse.DetailBarangResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/detailbarang"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idbarang", id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), DetailBarangResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
    }
    fun getVariasi(
        id: String,
        callback: VariasiResponse.VariasiResponseCallback
    ) {
        requestUrl = BASE_URL + "barang/variasi"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idbarang", id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e(TAG, "onResponse: $response")

                    val response1 =
                        gson.fromJson(response.toString(), VariasiResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }
                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)

                }
            })
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
        requestUrl = BASE_URL + "penjualan/create"

        AndroidNetworking.post(requestUrl)
            .addBodyParameter("idpenjualan", idpenjualan)
            .addBodyParameter("iduser", iduser)
            .addBodyParameter("total", total)
            .addBodyParameter("catatan", catatan)
            .addBodyParameter("pembayaran", pembayaran)
            .addBodyParameter("produk", produk)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("$TAG 11", "onResponse cekout: $response")
                    val response1 =
                        gson.fromJson(response.toString(), CheckoutResponse::class.java)
                    if (response1 != null) {
                        callback.onSuccess(response1)
                    } else {
                        callback.onFailure("Cannot get Object")
                    }

                }

                override fun onError(anError: ANError) {
                    callback.onFailure(anError.errorBody)
                    Log.e(TAG, "onError: $anError.errorBody")

                }
            })
    }


}

