package com.example.jol.flickr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView


class DownloadPhotoTask(internal var bitMapImage: ImageView?) : AsyncTask<String, Void, Bitmap>() {

    override fun doInBackground(vararg urls: String): Bitmap? {
        val urldisplay = urls[0]
        var image: Bitmap? = null
        try {
            val inputStream = java.net.URL(urldisplay).openStream()
            image = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }
        return image
    }

    override fun onPostExecute(result: Bitmap) {
        val bmImage = bitMapImage?.let {
            it.setImageBitmap(result)
        }
    }
}