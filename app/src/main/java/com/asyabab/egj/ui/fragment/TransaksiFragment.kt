package com.asyabab.egj.ui.fragment

import ViewPagerAdapter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.asyabab.egj.R
import com.asyabab.egj.base.BaseFragment
import com.asyabab.egj.data.models.login.Datum
import com.google.android.material.tabs.TabLayout


class TransaksiFragment : BaseFragment(){
    var dataProfile = Datum()
    private var viewPagerAdapter: ViewPagerAdapter? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_transaksi, container, false)
        val viewPager: ViewPager = root.findViewById(R.id.frame_container)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)

        viewPagerAdapter!!.add(BelumBayarFragment(), "Belum Diproses")
        viewPagerAdapter!!.add(DikirimFragment(), "Dikirim")
        viewPagerAdapter!!.add(SelesaiFragment(), "Selesai")
        viewPagerAdapter!!.add(DibatalkanFragment(), "Dibatalkan")


        viewPager.adapter = viewPagerAdapter

        val tabs: TabLayout = root.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        return root
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        Toast.makeText(context, "Suksses", Toast.LENGTH_LONG)
//            .show()
//        val fragment: Fragment? = activity?.supportFragmentManager?.findFragmentById(R.id.frame_container)
//        fragment?.onActivityResult(requestCode, resultCode, data)
//    }
}