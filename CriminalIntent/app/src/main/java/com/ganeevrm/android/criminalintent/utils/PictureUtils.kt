package com.ganeevrm.android.criminalintent.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.roundToInt

class PictureUtils {
    companion object {
        fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
            //Reading the size of an image on a disk
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            val srcWidth = options.outWidth.toFloat()
            val srcHeight = options.outHeight.toFloat()

            //Determine how much to zoom out
            val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
                1
            } else {
                val heightScale = srcHeight / destHeight
                val widthScale = srcWidth / destWidth

                minOf(heightScale, widthScale).roundToInt()
            }

            //Reading and creating the final bitmap image
            return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            })
        }
    }
}