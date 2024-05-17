package com.asyabab.egj.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.multidex.BuildConfig
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.login.Datum
import com.asyabab.egj.data.models.login.LoginResponse
import com.asyabab.egj.ui.activity.FinalPembayaranActivity
import com.asyabab.egj.ui.activity.KelolaAdmin
import com.asyabab.egj.ui.activity.RincianBelumBayarActivity
import com.asyabab.egj.utils.*
import com.asyabab.egj.ui.activity.DaftarActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_dikirim.view.*
import kotlinx.android.synthetic.main.fragment_profil.view.*
import kotlinx.android.synthetic.main.popup_cancel.*
import java.util.*


class ProfilFragment : BaseFragment() {
    var dataProfile = Datum()
    private var anggotaAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_user,
        onBind = { view, data, position ->

            view.tvNama.text = data.name
            view.tvNomerTelepon.text = data.nohp
            view.onClick {
                requireActivity().launchActivity<DaftarActivity> {
                    putExtra("data", data)
                }
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profil, container, false)
        val gson = Gson()

        dataProfile = gson.fromJson(repository?.getProfile(), Datum::class.java)
        if (dataProfile.status=="Admin") {
            root.layoutAnggota.visibility = View.GONE
            root.layoutAdmin.visibility = View.VISIBLE

            root.btTambah.onClick {
                activity?.launchActivity<DaftarActivity> { }
            }
            root.rvUser.setVerticalLayout(false)
            root.rvUser.adapter = anggotaAdapter
            getAnggota()

        }else{
            root.btTambah.visibility=View.GONE
            root.layoutAnggota.visibility=View.VISIBLE
            root.layoutAdmin.visibility=View.GONE
            dataProfile.gambar?.let { root.iconProfil.loadImageFromResources(context, it) }
            root.tvNama.text = dataProfile.name
            root.tvNomerTelepon.text = dataProfile.nohp
            root.tvAlamat.text = dataProfile.alamat
        }
        root.btDataAdmin.onClick {
            requireActivity().launchActivity<KelolaAdmin> {
            }
        }

        root.btKeluar.onClick {
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
                        "Yakin ingin keluar?"
                btCancel.onClick {
                    dismiss()
                }

                btApply.setOnClickListener {
                    repository!!.logoutSession()
                    activity?.finish()
                    dismiss()
                }

                show()
            }

        }
//
//        root.btShareAplikasi.onClick {
//            shareApp(root)
//        }
        return root
    }

    fun shareApp(v: View?) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            var shareMessage =
                "\nBagikan Aplikasi ini agar saling mengingatkan pada kebaikan\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Pilih Satu"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(
                context,
                "Gagal Membagikan Aplikasi",
                Toast.LENGTH_SHORT
            )
            //e.toString();
        }
    }

    fun getAnggota() {
        repository!!.getAnggota(
            object : LoginResponse.LoginResponseCallback {
                override fun onSuccess(loginResponse: LoginResponse) {
                    if (loginResponse.status == true) {

                        anggotaAdapter.clearItems()
                        loginResponse.data?.let { anggotaAdapter.addItems(it) }

                    } else {
                        Toast.makeText(context, loginResponse.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(context, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }

}
