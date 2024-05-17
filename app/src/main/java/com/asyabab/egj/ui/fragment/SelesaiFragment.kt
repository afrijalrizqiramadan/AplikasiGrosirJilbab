package com.asyabab.egj.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.data.models.penjualan.Selesai
import com.asyabab.egj.ui.activity.RincianSelesaiActivity
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_selesai.*
import kotlinx.android.synthetic.main.fragment_selesai.framekosong
import kotlinx.android.synthetic.main.fragment_selesai.framemain
import kotlinx.android.synthetic.main.fragment_selesai.frameshimmer
import kotlinx.android.synthetic.main.fragment_selesai.swipeResfresh
import kotlinx.android.synthetic.main.fragment_selesai.view.*
import kotlinx.android.synthetic.main.fragment_selesai.view.swipeResfresh
import kotlinx.android.synthetic.main.rv_selesai.view.tvCaraPembayaran
import kotlinx.android.synthetic.main.rv_selesai.view.tvHargaTotal
import kotlinx.android.synthetic.main.rv_selesai.view.tvNamaBarang
import kotlinx.android.synthetic.main.rv_selesai.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_selesai.view.tvVarianBarang
import java.util.*

class SelesaiFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var selesaiAdapter: RecyclerViewAdapter<Selesai> = RecyclerViewAdapter(
        R.layout.rv_selesai,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = "#" + data.id
            if (dataProfile.status=="Admin"){
                view.tvNamaBarang.text = data.user?.get(0)?.nama
                try {
                    view.tvVarianBarang.text = data.barang?.get(0)?.namaBarang
                } catch (e: IndexOutOfBoundsException) {
                    view.tvVarianBarang.text = "Barang Dihapus"
                } catch (e: InputMismatchException) {
                    view.tvVarianBarang.text = "Barang Dihapus"
                }
            }else{
                if(data.barang?.size==0){
                    view.tvVarianBarang.text = "Barang Dihapus"
                }else
                {
                view.tvNamaBarang.text = data.barang?.get(0)?.namaBarang
                view.tvVarianBarang.text = data.barang?.get(0)?.warnaStok+ " "+data.barang?.get(0)?.ukuranStok
            }}
            view.tvCaraPembayaran.text = data.metodepembayaran
            view.tvHargaTotal.text = data.totalHarga

            view.onClick {
                requireActivity().launchActivity<RincianSelesaiActivity> {
                    putExtra("data", data)
                }

            }
        })
    var dataProfile = com.asyabab.egj.data.models.login.Datum()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_selesai, container, false)

        root.rvSelesai.setVerticalLayout(true)
        root.rvSelesai.adapter = selesaiAdapter

        val gson = Gson()
        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )
        if (dataProfile.status=="Admin"){
            getPembelianAll()
        }else{
            getPembelian()
        }

        root.swipeResfresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if (dataProfile.status == "Admin") {
                getPembelianAll()
            } else {
                getPembelian()
            }
            swipeResfresh.isRefreshing = false;

        })
        return root
    }

    fun getPembelian() {
        repository!!.getPembelian(
            repository?.getToken()!!,
            object : PenjualanResponse.PenjualanResponseCallback {
                override fun onSuccess(penjualanResponse: PenjualanResponse) {
                    try {

                        if (penjualanResponse.status == true) {
                        selesaiAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.selesai?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.selesai?.let { selesaiAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }

                    }

                    } catch (e: IllegalStateException) {
                        //catch the IllegalStateExeption
                    } catch (e: Exception) {
                        //catch all the ones I didn't think of.
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
    fun getPembelianAll() {
        repository!!.getPembelianAll(
            object : PenjualanResponse.PenjualanResponseCallback {
                override fun onSuccess(penjualanResponse: PenjualanResponse) {
                    try {
                    if (penjualanResponse.status == true) {
                        selesaiAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.selesai?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.selesai?.let { selesaiAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvSelesai.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }

                    }
                } catch (e: IllegalStateException) {
                    //catch the IllegalStateExeption
                } catch (e: Exception) {
                    //catch all the ones I didn't think of.
                }}

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}