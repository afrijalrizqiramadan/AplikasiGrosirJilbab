package com.asyabab.egj.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.barang.BarangResponse
import com.asyabab.egj.data.models.barang.Datum
import com.asyabab.egj.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.egj.ui.activity.pilihproduk.DetailProdukActivity
import com.asyabab.egj.ui.activity.tambahproduk.TambahProdukActivity
import com.asyabab.egj.ui.activity.cart.TasBelanjaActivity
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_produk.*
import kotlinx.android.synthetic.main.fragment_produk.framemain
import kotlinx.android.synthetic.main.fragment_produk.frameshimmer
import kotlinx.android.synthetic.main.fragment_produk.panelKosong
import kotlinx.android.synthetic.main.fragment_produk.view.*
import kotlinx.android.synthetic.main.list_spinner.view.*
import kotlinx.android.synthetic.main.popup_varian.*
import kotlinx.android.synthetic.main.rv_produk.view.*
import java.text.DecimalFormat

class ProdukFragment : BaseFragment() {
    var dataProfile = com.asyabab.egj.data.models.login.Datum()
    var filter: List<Datum> = ArrayList()
    var varian = ""
    lateinit var dialog: Dialog

    private var produkAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_produk,
        onBind = { view, data, position ->
                 view.tvNama.text = data.namaBarang
                view.tvHarga.text = data.hargaStok?.toInt()?.convertRupiah()
            view.tvStok.text = "Stok "+data.stok


            data.namaGambar?.let { view.tvGambar.loadImageFromResources(context, it) }
            view.onClick {
                requireActivity().launchActivity<DetailProdukActivity> {
                    putExtra("data", data.idBarang.toString())
                    putExtra("gambar", data.namaGambar.toString())
                }
            }
        })
    private var kategoriAdapter: RecyclerViewAdapter<com.asyabab.egj.data.models.kategori.listkategori.Datum> = RecyclerViewAdapter(
        R.layout.list_spinner,
        onBind = { view, data, position ->
            view.text1.text = data.name

            view.onClick {

                data.name?.let { setFilterProduk(it) }
                produkAdapter.notifyDataSetChanged()
                dialog.dismiss()

            }
        })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_produk, container, false)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        root.rvProduk.layoutManager = layoutManager
        root.rvProduk.adapter = produkAdapter
        val gson = Gson()
        dataProfile = gson.fromJson(repository?.getProfile(), com.asyabab.egj.data.models.login.Datum::class.java)
        if (dataProfile.status=="Admin"){
            root.btTambah.visibility=(View.VISIBLE)
            root.btKeranjang.visibility=(View.GONE)
            root.btTambah.onClick {
                requireActivity().launchActivity<TambahProdukActivity> {  }
            }
        }else{
            root.btTambah.visibility=(View.GONE)
            root.btKeranjang.visibility=View.VISIBLE

        }
        root.btFilter.onClick {
            dialog = Dialog(context)
            dialog.apply {
                setContentView(R.layout.popup_varian)
                window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                rvKategori.setVerticalLayout(false)
                rvKategori.adapter = kategoriAdapter

                show()
            }
        }
        root.btKeranjang.onClick {
            requireActivity().launchActivity<TasBelanjaActivity> {  }
        }
        root.etCari.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString())
            }
        })
        setKategori()
        setProduk()
        return root
    }
    private fun filter(text: String) {
        //new array list that will hold the filtered data
        var filterdNames: ArrayList<Datum> = ArrayList()

        //looping through existing elements
        for (s in filter) {
            //if the existing elements contains the search input
            if (s.namaBarang?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }

        produkAdapter.clearItems()
        produkAdapter.addItems(filterdNames)
    }
    fun setKategori() {
        repository?.getListKategori(
            object : ListKategoriResponse.ListKategoriResponseCallback {
                override fun onSuccess(kategoriResponse: ListKategoriResponse) {
                    if (kategoriResponse.status == true) {
                        kategoriAdapter.clearItems()
                        kategoriResponse.data?.let { kategoriAdapter.addItems(it) }

                    } else {
                        Toast.makeText(context, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    Toast.makeText(context, "Gagal Mengambil Data", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }
    fun Any.convertRupiah(): String {
        val df = DecimalFormat("#,###,##0")


        val strFormat = df.format(this)
        var bilangan = "Rp " + strFormat
        return bilangan
    }
    fun setProduk(
    ) {
        repository!!.setBarang(
            object : BarangResponse.BarangResponseCallback {
                override fun onSuccess(barangResponse: BarangResponse) {
                    try {
                        if (barangResponse.status == true) {
                            if (barangResponse.data?.size.toString() == "0") {

                                frameshimmer.visibility = View.GONE
                                rvProduk.visibility = View.GONE
                                framemain.visibility = View.VISIBLE
                                panelKosong.visibility= View.VISIBLE
                            } else {
                                produkAdapter.clearItems()
                                barangResponse.data?.let { produkAdapter.addItems(it) }
                                filter= barangResponse.data!!
                                framemain.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvProduk.visibility = View.VISIBLE
                                panelKosong.visibility= View.GONE
                            }
                        } else {
                            panelKosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            framemain.visibility=View.VISIBLE
                        }
                    } catch (e: IllegalStateException) {
                        //catch the IllegalStateExeption
                    } catch (e: Exception) {
                        //catch all the ones I didn't think of.
                    }

                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun setFilterProduk(nama:String) {
        framemain.visibility = View.GONE
        frameshimmer.visibility = View.VISIBLE
        rvProduk.visibility = View.GONE

        repository!!.filterbarang(
            nama,
            object : BarangResponse.BarangResponseCallback {
                override fun onSuccess(barangResponse: BarangResponse) {
                    if (barangResponse.status == true) {
                        produkAdapter.clearItems()
                        if (barangResponse.data?.size.toString() == "0") {
                            frameshimmer.visibility = View.GONE
                            rvProduk.visibility = View.GONE
                            framemain.visibility = View.VISIBLE
                            panelKosong.visibility = View.GONE

                        } else {
                            barangResponse.data?.let { produkAdapter.addItems(it) }
                            filter= barangResponse.data!!
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvProduk.visibility = View.VISIBLE
                            panelKosong.visibility = View.GONE

                        }
                    } else {
                        panelKosong.visibility = View.VISIBLE
                        frameshimmer.visibility = View.GONE
                        framemain.visibility=View.VISIBLE
                    }
                }

                override fun onFailure(message: String) {
                    Log.e("Hasil", "Gagal Memuat" + message)
                    Toast.makeText(context, "Gagal Memuat" + message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}