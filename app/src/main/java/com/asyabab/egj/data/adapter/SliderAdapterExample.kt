package com.asyabab.egj.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asyabab.egj.R
import com.asyabab.egj.data.adapter.SliderAdapterExample.SliderAdapterVH
import com.asyabab.egj.data.models.item.detailitem.Image
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapterExample(private val context: Context) :

    SliderViewAdapter<SliderAdapterVH>() {
    private var mSliderItems: MutableList<Image> =
        ArrayList()

    fun renewItems(sliderItems: MutableList<Image>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Image) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_layout_item, null)


        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(
        viewHolder: SliderAdapterVH,
        position: Int
    ) {
        val sliderItem = mSliderItems[position]
//        viewHolder.textViewDescription.setText(sliderItem.name)
//        viewHolder.textViewDescription.textSize = 16f
//        viewHolder.textViewDescription.setTextColor(Color.WHITE)
//        Glide.with(viewHolder.itemView)
//            .load((sliderItem.name))
//            .fitCenter()
//            .into(viewHolder.imageViewBackground)
//

        viewHolder.itemView.setOnClickListener(View.OnClickListener {


//            val intent = Intent(context, JelajahActivity::class.java)
//            intent.putExtra("fragment", 1);
//            intent.putExtra("string1", sliderItem.id.toString());
//            intent.putExtra("string2", sliderItem.name.toString());
//            context.startActivity(intent)
//            Toast.makeText(
//                context,
//                "This is item in position $position",
//                Toast.LENGTH_SHORT
//            ).show()
        })


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


}
