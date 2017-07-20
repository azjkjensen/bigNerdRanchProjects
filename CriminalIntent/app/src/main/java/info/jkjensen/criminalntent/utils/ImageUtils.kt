package info.jkjensen.criminalntent.utils

import android.graphics.Bitmap
import android.R.attr.path
import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Point


/**
 * Created by jk on 7/20/17.
 */

fun getScaledBitmap(path:String, activity:Activity): Bitmap? {
    val size: Point = Point()
    activity.windowManager.defaultDisplay.getSize(size)
    return getScaledBitmap(path, size.x, size.y)
}

private fun getScaledBitmap(path:String, destWidth:Int, destHeight:Int): Bitmap? {
    var options: BitmapFactory.Options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth: Float = options.outWidth.toFloat()
    val srcHeight: Float = options.outHeight.toFloat()

    // Figure out how much to scale down by
    var inSampleSize = 1
    if (srcHeight > destHeight || srcWidth > destWidth) {
        val heightScale: Float = srcHeight / destHeight
        val widthScale: Float = srcWidth / destWidth

        inSampleSize = Math.round(if (heightScale > widthScale) heightScale else widthScale)
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    // Read in and create final bitmap
    return BitmapFactory.decodeFile(path, options)

}