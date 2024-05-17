package com.asyabab.egj.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.penjualan.Kirim
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.ui.activity.*
import com.asyabab.egj.utils.*
import com.google.gson.Gson

import kotlinx.android.synthetic.main.fragment_dikirim.*
import kotlinx.android.synthetic.main.fragment_dikirim.view.*
import kotlinx.android.synthetic.main.rv_dikirim.view.*

class DikirimFragment : BaseFragment() {

    private var kirimAdapter: RecyclerViewAdapter<Kirim> = RecyclerViewAdapter(
        R.layout.rv_dikirim,
        onBind = { view, data, position ->


            view.tvNoPesanan.text = "#" + data.id
             if (dataProfile.status=="Admin"){
                view.tvNamaBarang.text = data.user?.get(0)?.nama
                 if(data.barang?.size==0){
                     view.tvVarianBarang.text = "Barang Dihapus"
                 }else
                 {
                     view.tvVarianBarang.text = data.barang?.get(0)?.namaBarang
                     view.onClick {
                         requireActivity().launchActivity<RincianDikirimActivity> {
                             putExtra("data", data)
                         }
                     }
                 }
            }else{
                 if(data.barang?.size==0){
                     view.tvVarianBarang.text = "Barang Dihapus"
                 }else
                 {
                     view.tvNamaBarang.text = data.barang?.get(0)?.namaBarang
                     view.tvVarianBarang.text = data.barang?.get(0)?.warnaStok+ " "+data.barang?.get(0)?.ukuranStok
                     view.onClick {
                         requireActivity().launchActivity<RincianDikirimActivity> {
                             putExtra("data", data)
                         }
                     }
                 }
            }
            view.onClick {
                requireActivity().launchActivity<RincianDikirimActivity> {
                    putExtra("data", data)
                }

            }

            view.tvCaraPembayaran.text = data.metodepembayaran
            view.tvHargaTotal.text = data.totalHarga
        })

    var dataProfile = com.asyabab.egj.data.models.login.Datum()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dikirim, container, false)

        root.rvDikirim.setVerticalLayout(false)
        root.rvDikirim.adapter = kirimAdapter

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
                        kirimAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.kirim?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikirim.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.kirim?.let { kirimAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikirim.visibility = View.VISIBLE
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
                        kirimAdapter.clearItems()

                        if (penjualanResponse.data?.get(0)?.kirim?.size.toString() == "0") {
                            framekosong.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikirim.visibility = View.GONE
                            framemain.visibility = View.VISIBLE

                        } else {
                            penjualanResponse.data?.get(0)?.kirim?.let { kirimAdapter.addItems(it) }
                            framemain.visibility = View.VISIBLE
                            frameshimmer.visibility = View.GONE
                            rvDikirim.visibility = View.VISIBLE
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
}