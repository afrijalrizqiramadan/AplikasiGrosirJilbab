package com.asyabab.egj.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.utils.RequestPermission
import com.asyabab.egj.utils.getAppColor
import com.asyabab.egj.utils.launchActivity
import com.asyabab.egj.utils.lightStatusBar
import com.google.firebase.auth.FirebaseUser


class SplashScreenActivity : BaseActivity() {
    lateinit var handler: Handler
    private lateinit var smsAndStoragePermissionHandler: RequestPermission

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_loading)
        lightStatusBar(getAppColor(R.color.colorPrimary))
        smsAndStoragePermissionHandler = RequestPermission(this@SplashScreenActivity,
            permissions = setOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            listener = object : RequestPermission.Listener {
                override fun onComplete(
                    grantedPermissions: Set<String>,
                    deniedPermissions: Set<String>
                ) {
                    if (repository!!.cekLogin()){
                        val viewIntent = Intent(applicationContext, MainActivity::class.java)
                        viewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                        applicationContext?.startActivity(viewIntent)
                        finish()
                    }else
                    {
                        val viewIntent = Intent(applicationContext, LoginActivity::class.java)
                        viewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                        applicationContext?.startActivity(viewIntent)
                        finish()

                    }
                }

                override fun onShowPermissionRationale(permissions: Set<String>): Boolean {
                    AlertDialog.Builder(this@SplashScreenActivity, R.style.AlertDialogTheme)
                        .setMessage("Aplikasi Meminta Izin")
                        .setPositiveButton("OK") { _, _ ->
                            smsAndStoragePermissionHandler.retryRequestDeniedPermission()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            smsAndStoragePermissionHandler.cancel()
                            dialog.dismiss()
                        }
                        .show()



                    return true // don't want to show any rationale, just return false here
                }

                override fun onShowSettingRationale(permissions: Set<String>): Boolean {
                    AlertDialog.Builder(this@SplashScreenActivity, R.style.AlertDialogTheme)
                        .setMessage("Masuk ke pengaturan lalu pilih izin aplikasi")
                        .setPositiveButton("Settings") { _, _ ->
                            smsAndStoragePermissionHandler.requestPermissionInSetting()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            smsAndStoragePermissionHandler.cancel()
                            dialog.cancel()
                        }
                        .show()
                    return true
                }
            })


        handler = Handler()
        handler.postDelayed({
            handleRequestPermission()


        }, 2000)
    }

    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.e("hasil", user.displayName.toString())
            launchActivity<MainActivity>()
            finish()

        } else {
            launchActivity<LoginActivity>()
            finish()
        }
    }

    private fun handleRequestPermission() {
        smsAndStoragePermissionHandler.requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        smsAndStoragePermissionHandler.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        smsAndStoragePermissionHandler.onActivityResult(requestCode)
    }
//    fun login(email: String, uid: String) {
//        repository!!.login(email, uid, object : LoginResponse.LoginResponseCallback {
//            override fun onSuccess(loginResponse: LoginResponse) {
//                if (loginResponse.status == true) {
//                    val token = loginResponse.data!!.token
//                    repository!!.saveToken(token)
//                    Log.e("hasil", repository?.getToken().toString())
//                    if (loginResponse.data!!.lengkap.equals("YA")) {
//                        launchActivity<DPDashboardActivity>()
//                        finish()
//                    } else {
//                        launchActivity<DPSignUpStepsActivity> { }
//                        finish()
//                    }
//                } else {
//                    launchActivity<DPWalkThroughActivity>()
//                    finish()
//                }
//            }
//
//            override fun onFailure(message: String) {
//                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//                launchActivity<DPWalkThroughActivity>()
//                finish()
//            }
//
//        })
//    }

}