package com.example.jol.flickr

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

class PhotoGridAdapter(context: Context, var itemList: ArrayList<PhotoData>) : BaseAdapter() {
    var context: Context? = context

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val item = this.itemList[position]

        val inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflator.inflate(R.layout.photo_grid_cell, null)

        val imageView = itemView.findViewById<ImageView>(R.id.imgItem)
        imageView.let {
            DownloadPhotoTask(imageView).execute(constructURLForPhotoItem(item))
        }
        return itemView
    }

    private fun constructURLForPhotoItem(item: PhotoData): String {

        val farmId = item.farm
        val serverId = item.server
        val id = item.id
        val secret = item.secret

        return "https://farm" + farmId +
                ".staticflickr.com/" +
                serverId + "/" +
                id +
                "_" +
                secret + ".jpg"

    }

    override fun getItem(position: Int) = this.itemList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = itemList.size / 4

}
