package com.asyabab.egj.ui.activity.tambahproduk

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.kategori.listkategori.Datum
import com.asyabab.egj.data.models.kategori.listkategori.ListKategoriResponse
import com.asyabab.egj.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_tambahkategori.*
import kotlinx.android.synthetic.main.list_simple.view.*
import kotlinx.android.synthetic.main.list_spinner.view.*
import kotlinx.android.synthetic.main.list_spinner.view.text1
import kotlinx.android.synthetic.main.popup_tambahkategori.*


class TambahKategoriActivity : BaseActivity() {
    lateinit var handler: Handler
    private var kategoriAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.list_simple,
        onBind = { view, data, position ->
            view.text1.text = data.name

            view.onClick {
                repository!!.saveData("kategori",data.id.toString())
                repository!!.saveData("namakategori",data.name.toString())
                finish()
            }
            view.btHapus.onClick {
                hapusKategori(data.id.toString())
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahkategori)
        rvKategori.setVerticalLayout(false)
        rvKategori.adapter = kategoriAdapter
        setKategori()

        btTambahKategori.onClick {
            val btnsheet = layoutInflater.inflate(R.layout.popup_tambahkategori, null)
            val dialog = BottomSheetDialog(this@TambahKategoriActivity)
            dialog.setContentView(btnsheet)

            dialog.btApply.onClick {
                tambahKategori(dialog.etKategori.text.toString())
                dialog.dismiss()
            }

            dialog.btCancel.onClick {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    fun setKategori() {
        repository?.getListKategori(
            object : ListKategoriResponse.ListKategoriResponseCallback {
                override fun onSuccess(kategoriResponse: ListKategoriResponse) {
                    loading?.dismiss()
                    if (kategoriResponse.status == true) {
                        kategoriAdapter.clearItems()
                        kategoriResponse.data?.let { kategoriAdapter.addItems(it) }

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
    fun tambahKategori(nama:String) {
        repository?.tambahKategori(
            nama,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        setKategori()
                        kategoriAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Berhasil", Toast.LENGTH_LONG)
                            .show()
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
    fun hapusKategori(nama:String) {
        repository?.hapusKategori(
            nama,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        setKategori()
                        kategoriAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Berhasil", Toast.LENGTH_LONG)
                            .show()
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
}