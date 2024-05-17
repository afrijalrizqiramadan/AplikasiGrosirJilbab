package com.asyabab.egj.ui.activity


import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );
        val navController = findNavController(R.id.frame_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_transaksi,
                R.id.navigation_produk,
                R.id.navigation_profil
            )
        )
        navView.setupWithNavController(navController)

    }

}