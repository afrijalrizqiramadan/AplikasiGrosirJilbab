package com.asyabab.egj.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.asyabab.egj.App
import com.asyabab.egj.R
import com.asyabab.egj.data.Repository
import com.asyabab.egj.utils.getAppColor
import com.asyabab.egj.utils.lightStatusBar
import com.asyabab.egj.utils.onClick
import com.asyabab.egj.utils.DialogHelper
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.toolbar_normal.*
import java.io.Serializable

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar(getAppColor(R.color.dp_app_background))
        progressBar = DialogHelper.loading(this)

        if (btBack != null) {
            btBack.onClick {
                finish()
            }
        }

//        repository?.saveToken(
//            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiNDIwZTMwNjAxNzYyMmRkY2NlOGY0MTU2NDYzY2YyYjNiMjRiNmY1ODdmMTQwYjA2NTVmMmMxMjQxOTc1NTA2Y2M1OWM3MjFiNzZlY2QyNGQiLCJpYXQiOiIxNjA4MDI1ODU4LjQyNjM5MCIsIm5iZiI6IjE2MDgwMjU4NTguNDI2MzkzIiwiZXhwIjoiMTYzOTU2MTg1OC40MjI4NDgiLCJzdWIiOiIxMiIsInNjb3BlcyI6WyJjdXN0b21lciJdfQ.WRmm_uTTZjQ1XMR0OcMM7n9FxY0XxvDn9vkBJ5dyBdhEQ04n5SeHxWMuZ-_UUmeaYbmns_C0N3E7E5OHoQ8IqUZtTYtjd7EMTU0R8buBJx8l05Mc1S7F1Skeq6rZDJyRmCsyZz7FrXZP3ePgMwwmpNIjqGvu_z0xzzN-xe5JHXRpbTC_ZzjvxhPy5hRc3qApYV_cAdl7InlWLCUYGCsk0OtkViYsBTG3lP1ewGMgquAue2vm8bP0IoSgguMA4CGchriyoEq-kZ0fQJdcLnP6_RGPXRPGAqarjfwQue3fCvGIw6qtv-noTq4MxuyMbMrqdTT3GPWfPKZtIXNKnfNC5EhRdy1J-f6Ym8HjVQueLVJDvcSnzlG0UbaHVY85v_EaZtau5QVbjRCapGW6j_SkjmyWQ0B0hCpetUOWVJ3dHd0Giafl8td1fCR6i9Isz66VXKI2AEWcsux6yRQw6z72DEfeM2u1CeftEIqfrTYr6cPMf__RpsWN1wM6FF7XmppGS8LQrlzn0C9SzU19lnKMJWk7TMJoAFJLzN8dV4_7vabyNVWIYaVCSfmz3vpwGzzHPOLtPyZK3rCJrYPiM0CBmQuRpoMm8sS35r4yFg9L464HiPET4hgP_1mlkbcc1GsryRCUKAqXLna8KsCilimX5JBqq0bTv88ObmHKFk35h20"
//        )
    }

    protected val mAuth: FirebaseAuth?
        get() = FirebaseAuth.getInstance()
    protected val mLoginManager: LoginManager?
        get() = LoginManager.getInstance()
    protected val loading: AlertDialog?
        get() = progressBar


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    protected val repository: Repository?
        get() = (application as App).repository


/*
    fun setToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationIcon(R.drawable.theme9_ic_arrow_back)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.changeToolbarFont()
    }
*/


    fun loadFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit()


        }
    }

    fun loadFragmentTag(fragment: Fragment?, tag: String) {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment, tag)
                .commit()


        }
    }

    fun loadFragment(fragment: Fragment?, uid: String) {
        if (fragment != null) {
            val bundle = Bundle()
            bundle.putString("uid", uid)
            fragment.arguments = bundle
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .detach(fragment).attach(fragment)
                .commit()
        }
    }


    fun loadFragment(fragment: Fragment?, serializable: Serializable) {
        if (fragment != null) {
            val bundle = Bundle()
            bundle.putSerializable("serial", serializable)
            fragment.arguments = bundle
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit()
        }
    }

    fun loadFragment(fragment: Fragment?, string1: String, string2: String) {
        if (fragment != null) {
            val bundle = Bundle()
            bundle.putString("string1", string1)
            bundle.putString("string2", string2)
            fragment.arguments = bundle
            supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.frame_container, fragment)
                ?.detach(fragment)?.attach(fragment)
                ?.commit()
        }
    }


    fun loadFragmentBack(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)

                .commit()
        }
    }

}
