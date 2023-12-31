package com.akeso.depthestimationapp.utils

import android.content.Context
import android.graphics.*
import android.media.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

// Helper class for operations on Bitmaps
object BitmapUtils {

    // Rotate the given `source` by `degrees`.
    fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
    }


    // Use this method to save a Bitmap to the internal storage ( app-specific storage ) of your device.
    fun saveBitmap(context: Context, image: Bitmap, name: String) {
        val fileOutputStream =
            FileOutputStream(File(context.filesDir.absolutePath + "/$name.png"))
        image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
    }


    // Resize the given Bitmap with given `targetWidth` and `targetHeight`.
    fun resizeBitmap(src: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(src, targetWidth, targetHeight, true)
    }


    // Flip the given `Bitmap` vertically.
    fun flipBitmap(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


    fun byteBufferToBitmap(imageArray: FloatArray, imageDim: Int): Bitmap {
        val pixels = imageArray.map { it.toInt() }.toIntArray()
        val bitmap = Bitmap.createBitmap(imageDim, imageDim, Bitmap.Config.RGB_565);
        for (i in 0 until imageDim) {
            for (j in 0 until imageDim) {
                val p = pixels[i * imageDim + j]
                bitmap.setPixel(j, i, Color.rgb(p, p, p))
            }
        }
        return bitmap
    }


    // Convert android.media.Image to android.graphics.Bitmap and rotate it by `rotationDegrees`
    fun imageToBitmap(image: Image, rotationDegrees: Int): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val yuv = out.toByteArray()
        return rotateBitmap(
            BitmapFactory.decodeByteArray(yuv, 0, yuv.size),
            rotationDegrees.toFloat()
        )
    }
}