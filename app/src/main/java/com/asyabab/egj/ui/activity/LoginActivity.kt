package com.asyabab.egj.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.login.LoginResponse
import com.asyabab.egj.utils.AndroidBug5497Workaround
import com.asyabab.egj.utils.launchActivityWithNewTask
import com.asyabab.egj.utils.onClick
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_masuk.*


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        AndroidBug5497Workaround.assistActivity(this, R.id.content_area)

        btMasuk.onClick {
            if (inputUsername.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    "Masukkan Username",
                    Toast.LENGTH_LONG
                ).show()
            } else if (inputPassword.text.toString() == "") {
                Toast.makeText(
                    applicationContext,
                    "Masukkan Password",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                tvGagalMasuk.visibility = View.INVISIBLE
                loading?.show()

                login(
                    inputUsername.text.toString(),
                    inputPassword.text.toString()
                )
            }
        }

    }


    fun login(
        email: String,
        password: String
    ) {
        repository?.login(
            email,
            password,
            object : LoginResponse.LoginResponseCallback {
                override fun onSuccess(loginResponse: LoginResponse) {
                    loading?.dismiss()
                    if (loginResponse.status == true) {
                            val gson = Gson()
                            val json: String = gson.toJson(loginResponse.data?.get(0))
                        repository?.setLogin(true)
                        repository?.saveToken(loginResponse.data?.get(0)?.id)

                        repository?.saveProfile(json)
                            Log.d("Losgin", json)
                            Toast.makeText(applicationContext, "Berhasil Login", Toast.LENGTH_LONG)
                                .show()
                            launchActivityWithNewTask<MainActivity>()
                            finish()
                    } else {
                        Toast.makeText(applicationContext, loginResponse.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.d("Login", message)
                    loading?.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Login Gagal",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}

