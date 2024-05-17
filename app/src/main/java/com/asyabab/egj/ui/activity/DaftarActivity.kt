package com.asyabab.egj.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.Secure
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.asyabab.egj.data.models.general.GeneralResponse
import com.asyabab.egj.data.models.login.Datum
import com.asyabab.egj.data.models.penjualan.Prose
import com.asyabab.egj.data.models.register.RegisterResponse
import com.asyabab.egj.utils.ImagePickerActivity
import com.asyabab.egj.utils.launchActivityWithNewTask
import com.asyabab.egj.utils.loadImageFromResources
import com.asyabab.egj.utils.onClick
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_daftar.tvTitle
import kotlinx.android.synthetic.main.fragment_profil.view.*
import kotlinx.android.synthetic.main.popup_cancel.*
import java.io.File
import java.io.IOException


class DaftarActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var jenisSpinner: Spinner? = null
    val REQUEST_IMAGE = 100
    private val TAG: String =DaftarActivity::class.java.simpleName
    var dataProfile = Datum()

    private val jenis =
        arrayOf("Anggota","Admin")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)
        jenisSpinner = findViewById(R.id.inputJenis)
        jenisSpinner?.onItemSelectedListener = this
        val gson = Gson()

        try {
            dataProfile = intent.getSerializableExtra("data") as Datum
            var value=""
            value= dataProfile.id.toString()
            dataProfile.id?.let { Log.d("Berhasil", it) }

            if (value!=""){
                dataProfile.id?.let { Log.d("Berhasil", it) }
                inputNama.setText(dataProfile.name)
                inputNohp.setText(dataProfile.nohp)
                inputAlamat.setText(dataProfile.alamat)
                inputPassword.setText(dataProfile.password)
                inputPasswordUlang.setText(dataProfile.password)
                tvTitle.text = "Edit Profil"
                inputUsername.setText(dataProfile.username)
                iconProfil.isEnabled = false
                inputJenis.visibility=View.INVISIBLE

                dataProfile.gambar?.let { iconProfil.loadImageFromResources(this, it) }
                btDaftar.text="UPDATE"
                btDaftar.onClick {
                    val id =dataProfile.id
                    val nama = inputNama.text.toString()
                    val nohp = inputNohp.text.toString()
                    val alamat = inputAlamat.text.toString()
                    val password = inputPassword.text.toString()
                    val passwordulang = inputPasswordUlang.text.toString()
                    val username = inputUsername.text.toString()

                    if (username == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan Username",
                            Toast.LENGTH_LONG
                        ).show()

                    } else if (username.length < 6) {
                        Toast.makeText(
                            applicationContext,
                            "Username Minimal 6 Karakter",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (nama == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan Nama",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if (nohp == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan No HP",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    else if (alamat == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan Alamat",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    else if (password == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan Kata Sandi",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (passwordulang == "") {
                        Toast.makeText(
                            applicationContext,
                            "Masukkan Ulang Kata Sandi",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (passwordulang != password) {
                        Toast.makeText(
                            applicationContext,
                            "Password Harus Sama",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        loading?.show()
                        updateUser(id!!,nama, alamat, nohp, username, password)
                    }
                }
                btHapus.visibility=View.VISIBLE
                btHapus.onClick {
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
                                "Yakin ingin Menghapus?"
                        btCancel.onClick {
                            dismiss()
                        }

                        btApply.setOnClickListener {
                            dataProfile.id?.let { deleteUser(it) }

                            dismiss()
                        }

                        show()
                    }

                }
            }else{

            }
        } catch (e: Exception) {
            iconProfil.onClick {
                onProfileImageClick()
            }

            btHapus.visibility=View.INVISIBLE
            btDaftar.onClick {
                val status =inputJenis.selectedItem.toString()
                val nama = inputNama.text.toString()
                val nohp = inputNohp.text.toString()
                val alamat = inputAlamat.text.toString()
                val password = inputPassword.text.toString()
                val passwordulang = inputPasswordUlang.text.toString()
                val username = inputUsername.text.toString()

                if (uri.toString().isNullOrEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Gambar",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else if (username == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Username",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else if (username.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        "Username Minimal 6 Karakter",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (nama == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Nama",
                        Toast.LENGTH_LONG
                    ).show()
                }else if (nohp == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan No HP",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else if (alamat == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Alamat",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else if (password == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Kata Sandi",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (passwordulang == "") {
                    Toast.makeText(
                        applicationContext,
                        "Masukkan Ulang Kata Sandi",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (passwordulang != password) {
                    Toast.makeText(
                        applicationContext,
                        "Password Harus Sama",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else {

                    loading?.show()
                    register(nama, alamat, nohp, username, password, status, File(uri.path))
                }
            }
        }

        val adapterJenis = ArrayAdapter(
            this,
            R.layout.list_spinner, R.id.text1,
            jenis
        )
        adapterJenis.setDropDownViewResource(R.layout.list_spinner)
        jenisSpinner?.adapter = adapterJenis




    }
    var uri:Uri=Uri.parse("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data?.getParcelableExtra<Uri>("path")!!
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)

                    loadProfile(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun loadProfile(url: String) {
        Log.d(TAG,
            "Image cache path: $url"
        )
        Glide.with(this).load(url)
            .into(iconProfil)
        iconProfil.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent))
    }
    fun register(
        nama: String,
        alamat: String,
        nohp: String,
        username: String,
        password: String,
        status: String,
        img: File
    ) {
        repository?.register(
            nama,
            alamat,
            nohp,
            username,
            password,
            status,
            img,
            object : RegisterResponse.RegisterResponseCallback {
                override fun onSuccess(registerResponse: RegisterResponse) {
                    Log.d("Login", "signInsuccess")
                    loading?.dismiss()
//                    Log.d("Login", "" + registerResponse.message?.email.toString())

                    if (registerResponse.status == true) {
                        Log.d("Login", "signInsuccess2")
                        if (!TextUtils.isEmpty(registerResponse.data?.token.toString())) {
                            val token = registerResponse.data?.token.toString()
//                            repository?.saveToken(token)
                            finish()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Daftar Gagal",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.e(TAG, "respon register $message")

                    loading?.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Server Sedang Error",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }


        )
    }
    fun deleteUser(
        id: String
    ) {
        repository?.deleteUser(
            id,
            object : GeneralResponse.GeneralResponseCallback {
                override fun onSuccess(generalResponse: GeneralResponse) {
                    loading?.dismiss()
                    if (generalResponse.status == true) {
                        Log.d("Login", "signInsuccess2")
                        Toast.makeText(
                            applicationContext,
                            "Berhasil Dihapus",
                            Toast.LENGTH_LONG
                        ).show()
                        launchActivityWithNewTask<MainActivity>()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Hapus Gagal",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.e(TAG, "respon register $message")

                    loading?.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Jaringan Gangguan",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }


        )
    }
    fun updateUser(
        id: String,
        nama: String,
        alamat: String,
        nohp: String,
        username: String,
        password: String
    ) {
        repository?.updateUser(
            id,
            nama,
            alamat,
            nohp,
            username,
            password,
            object : RegisterResponse.RegisterResponseCallback {
                override fun onSuccess(registerResponse: RegisterResponse) {
                    Log.d("Login", "signInsuccess")
                    loading?.dismiss()

                    if (registerResponse.status == true) {
                        Log.d("Login", "signInsuccess2")
                        launchActivityWithNewTask<MainActivity>()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Daftar Gagal",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(message: String) {
                    Log.e(TAG, "respon register $message")

                    loading?.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Server Sedang Error",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }


        )
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@DaftarActivity)
        builder.setTitle("Izin Perangkat")
        builder.setMessage("Izin Perangkat")
        builder.setPositiveButton("Pergi ke Pengaturan") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }
    private fun onProfileImageClick() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showImagePickerOptions()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }
    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }
    private fun launchCameraIntent() {
        val intent = Intent(this@DaftarActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16) // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 9)
//
//        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@DaftarActivity, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, false)
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16) // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 9)
        startActivityForResult(intent, REQUEST_IMAGE)
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}