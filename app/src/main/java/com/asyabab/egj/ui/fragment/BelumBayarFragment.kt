package com.asyabab.egj.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.penjualan.PenjualanResponse
import com.asyabab.egj.data.models.penjualan.Prose
import com.asyabab.egj.ui.activity.MainActivity
import com.asyabab.egj.ui.activity.RincianBelumBayarActivity
import com.asyabab.egj.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_belumbayar.*
import kotlinx.android.synthetic.main.fragment_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_belumbayar.view.*
import kotlinx.android.synthetic.main.rv_belumbayar.view.tvCaraPembayaran
import kotlinx.android.synthetic.main.rv_belumbayar.view.tvHargaTotal
import kotlinx.android.synthetic.main.rv_belumbayar.view.tvNamaBarang
import kotlinx.android.synthetic.main.rv_belumbayar.view.tvNoPesanan
import kotlinx.android.synthetic.main.rv_belumbayar.view.tvVarianBarang
import java.util.*


class BelumBayarFragment : BaseFragment() {

    private var belumBayarAdapter: RecyclerViewAdapter<Prose> = RecyclerViewAdapter(
        R.layout.rv_belumbayar,
        onBind = { view, data, position ->
            view.tvNoPesanan.text = "#" + data.id
            if (dataProfile.status=="Admin"){
                try {
                    view.tvVarianBarang.text = data.barang?.get(0)?.namaBarang
                } catch (e: IndexOutOfBoundsException) {
                    view.tvVarianBarang.text = "Barang Dihapus"
                } catch (e: InputMismatchException) {
                    view.tvVarianBarang.text = "Barang Dihapus"
                }
                view.tvNamaBarang.text = data.user?.get(0)?.nama

            }else {
                if (data.barang?.size == 0) {
                    view.tvVarianBarang.text = "Barang Dihapus"
                } else {
                    view.tvNamaBarang.text = data.barang?.get(0)?.namaBarang
                    view.tvVarianBarang.text =
                        data.barang?.get(0)?.warnaStok + " " + data.barang?.get(0)?.ukuranStok
                    view.btProses.visibility = View.GONE
                }
            }
            view.tvCaraPembayaran.text = data.metodepembayaran
            view.tvHargaTotal.text = data.totalHarga
            view.btProses.onClick {
                setUpdatePenjualan(data.id!!, "kirim")
            }
            view.onClick {
                requireActivity().launchActivity<RincianBelumBayarActivity> {
                    putExtra("data", data)
                }

            }
        })
    var dataProfile = com.asyabab.egj.data.models.login.Datum()
    fun setUpdatePenjualan(id: String,
                           produk: String)
    {
        repository?.setUpdatePenjualan(
            id,
            produk,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    if (generalResponse.status == true) {
                        activity?.launchActivityWithNewTask<MainActivity>()
                        if (dataProfile.status=="Admin"){
                            getPembelianAll()
                        }else{
                            getPembelian()
                        }//                        setResult(10001);
//                        finish()

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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_belumbayar, container, false)
        root.rvBelumBayar.setVerticalLayout(false)
        root.rvBelumBayar.adapter = belumBayarAdapter

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

        root.swipeResfresh.setOnRefreshListener(OnRefreshListener {
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
                            belumBayarAdapter.clearItems()
                            if (penjualanResponse.data?.get(0)?.proses?.size.toString() == "0") {
                                framekosong.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvBelumBayar.visibility = View.GONE
                                framemain.visibility = View.VISIBLE
                            } else {
                                penjualanResponse.data?.get(0)?.proses?.let { belumBayarAdapter.addItems(it) }
                                framemain.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvBelumBayar.visibility = View.VISIBLE
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
                    try {

                        if (penjualanResponse.status == true) {
                            belumBayarAdapter.clearItems()

                            if (penjualanResponse.data?.get(0)?.proses?.size.toString() == "0") {
                                framekosong.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvBelumBayar.visibility = View.GONE
                                framemain.visibility = View.VISIBLE

                            } else {
                                penjualanResponse.data?.get(0)?.proses?.let { belumBayarAdapter.addItems(it) }
                                framemain.visibility = View.VISIBLE
                                frameshimmer.visibility = View.GONE
                                rvBelumBayar.visibility = View.VISIBLE
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