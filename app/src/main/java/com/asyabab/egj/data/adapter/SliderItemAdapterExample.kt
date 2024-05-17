package com.asyabab.egj.data.adapter

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.asyabab.egj.R
import com.asyabab.egj.data.adapter.SliderAdapterExample.SliderAdapterVH
import com.asyabab.egj.data.models.detailbarang.Gambar

import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import java.io.File
import java.util.*

class SliderItemAdapterExample(private val context: Context) :
    SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<Gambar> =
        ArrayList()

    fun renewItems(sliderItems: MutableList<Gambar>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Gambar) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterExample.SliderAdapterVH? {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, null)


        return SliderAdapterExample.SliderAdapterVH(inflate)
    }


    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    class SliderAdapterVH(itemView: View) :
        ViewHolder(itemView) {
        var itemVie: View
        var imageViewBackground: ImageView
        var imageGifContainer: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground =
                itemView.findViewById(R.id.iv_auto_image_slider)
            imageGifContainer =
                itemView.findViewById(R.id.iv_gif_container)
            textViewDescription = itemView.findViewById(R.id.tvdownload)
            this.itemVie = itemView
        }
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterExample.SliderAdapterVH?,
        position: Int
    ) {
        val sliderItem = mSliderItems[position]
        AndroidNetworking.initialize(context)

        //Folder Creating Into Phone Storage

        //Folder Creating Into Phone Storage
        dirPath = Environment.getExternalStorageDirectory().toString() + "/Gambar Produk"

        fileName = sliderItem.idGambar+".jpeg"

        file = File(dirPath, fileName)
        viewHolder?.itemView?.let {
            Glide.with(it)
                .load(("https://api.endahgrosirjilbab.com/storage/barang/"+ sliderItem.namaGambar))
                .fitCenter()
                .into(viewHolder.imageViewBackground)
        }
        viewHolder?.textViewDescription?.setOnClickListener {
            DownloadImageFromPath("https://api.endahgrosirjilbab.com/storage/barang/"+sliderItem.namaGambar)

        //            Toast.makeText(
//                context,
//                "This is item in position $position",
//                Toast.LENGTH_SHORT
//            ).show()
        }

    }
    var file: File? = null
    var dirPath: String? = ""
    var fileName: String? = ""

    fun DownloadImageFromPath(url: String?) {
        AndroidNetworking.download(url, dirPath, fileName)
            .build()
            .startDownload(object : DownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(context, "Download Berhasil, File Disimpan di Folder Gambar Produk", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(context, "Download Gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}
