package com.asyabab.egj.ui.activity


import android.os.Bundle
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.login.Datum
import com.asyabab.egj.data.models.login.LoginResponse
import com.asyabab.egj.utils.RecyclerViewAdapter
import com.asyabab.egj.utils.launchActivity
import com.asyabab.egj.utils.onClick
import com.asyabab.egj.utils.setVerticalLayout
import com.asyabab.egj.ui.activity.DaftarActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kelolaladmin.*
import kotlinx.android.synthetic.main.rv_user.view.*
import java.text.DecimalFormat

class KelolaAdmin : BaseActivity() {
    var id = ""

    var dataProfile = Datum()
    private var anggotaAdapter: RecyclerViewAdapter<Datum> = RecyclerViewAdapter(
        R.layout.rv_user,
        onBind = { view, data, position ->

            view.tvNama.text = data.name
            view.tvNomerTelepon.text = data.nohp
            view.onClick {
                launchActivity<DaftarActivity> {
                    putExtra("data", data)
                }
            }
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelolaladmin)

        val gson = Gson()

        dataProfile = gson.fromJson(
            repository?.getProfile(),
            com.asyabab.egj.data.models.login.Datum::class.java
        )

        btBack.onClick {
            finish()
        }

        rvAdmin.setVerticalLayout(false)
        rvAdmin.adapter = anggotaAdapter
        getAnggota()

    }
    fun getAnggota() {
        repository!!.getAdmin(
            object : LoginResponse.LoginResponseCallback {
                override fun onSuccess(loginResponse: LoginResponse) {
                    if (loginResponse.status == true) {

                        anggotaAdapter.clearItems()
                        loginResponse.data?.let { anggotaAdapter.addItems(it) }

                    } else {
                        Toast.makeText(this@KelolaAdmin, loginResponse.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Toast.makeText(this@KelolaAdmin, "Server Sedang Error", Toast.LENGTH_LONG).show()
                }

            })
    }

}