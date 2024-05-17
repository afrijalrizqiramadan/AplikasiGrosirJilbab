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
import com.asyabab.egj.data.models.penjualan.Dibatalkan
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.ui.activity.RincianDibatalkanActivity
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dibatalkan.*
import kotlinx.android.synthetic.main.fragment_dibatalkan.framekosong
import kotlinx.android.synthetic.main.fragment_dibatalkan.framemain
import kotlinx.android.synthetic.main.fragment_dibatalkan.frameshimmer
import kotlinx.android.synthetic.main.fragment_dibatalkan.swipeResfresh
import kotlinx.android.synthetic.main.fragment_dibatalkan.view.*
import kotlinx.android.synthetic.main.rv_dibatalkan.view.tvCaraPembayaran
import kotlinx.android.synthetic.main.rv_dibatalkan.view.tvHargaTotal
import kotlinx.android.synthetic.main.rv_dibatalkan.view.tvNamaBarang
import kotlinx.android.synthetic.main.rv_dibatalkan.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_dibatalkan.view.tvVarianBarang
import java.util.*


class DibatalkanFragment : BaseFragment() {
    private val viewPool = RecyclerView.RecycledViewPool()

    private var dibatalkanAdapter: RecyclerViewAdapter<Dibatalkan> = RecyclerViewAdapter(
        R.layout.rv_dibatalkan,
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
  }else {
      if (data.barang?.size == 0) {
          view.tvVarianBarang.text = "Barang Dihapus"
      } else {
          view.tvNamaBarang.text = data.barang?.get(0)?.namaBarang
          view.tvVarianBarang.text =
              data.barang?.get(0)?.warnaStok + " " + data.barang?.get(0)?.ukuranStok
      }
  }
             view.tvCaraPembayaran.text = data.metodepembayaran
            view.tvHargaTotal.text = data.totalHarga

            view.onClick {
                requireActivity().launchActivity<RincianDibatalkanActivity> {
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
        val root = inflater.inflate(R.layout.fragment_dibatalkan, container, false)
        root.rvDibatalkan.setVerticalLayout(false)
        root.rvDibatalkan.adapter = dibatalkanAdapter

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
            if (dataProfile.status=="Admin"){
                getPembelianAll()
            }else{
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
                        dibatalkanAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.dibatalkan?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.dibatalkan?.let { dibatalkanAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.VISIBLE
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
                    Toast.makeText(context, "Jaringan Bermasalah", Toast.LENGTH_LONG).show()
                }

            })
    }
    fun getPembelianAll() {
        repository!!.getPembelianAll(
            object : PenjualanResponse.PenjualanResponseCallback {
                override fun onSuccess(penjualanResponse: PenjualanResponse) {
                    if (penjualanResponse.status == true) {
                        dibatalkanAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.dibatalkan?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.dibatalkan?.let { dibatalkanAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDibatalkan.visibility = View.VISIBLE
                            framekosong.visibility = View.GONE

                        }

                    }

                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()

                }

            })
    }
}