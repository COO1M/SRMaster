package com.master.sr.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.master.sr.app.App
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    @Suppress("DEPRECATION")
    fun uri2bitmap(uri: Uri): Bitmap {
        val context = App.context
        val bitmap = if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        val width = bitmap.width
        val height = bitmap.height
        val maxSide = maxOf(width, height)
        return if (maxSide > 800) {
            val matrix = Matrix()
            val scale = 800f / maxSide
            matrix.setScale(scale, scale)
            Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        } else {
            bitmap
        }
    }

    fun saveBitmap(bitmap: Bitmap) {
        val context: Context = App.context
        val now = System.currentTimeMillis().toString()
        if (Build.VERSION.SDK_INT >= 29) {
            val resolver = context.contentResolver
            val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val details = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$now.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SRMaster")
            }
            val uri = resolver.insert(collection, details)
            if (uri != null) {
                resolver.openOutputStream(uri).use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        } else {
            val dir = "${Environment.getExternalStorageDirectory().absolutePath}/Pictures/SRMaster"
            File(dir).let { if (!it.exists()) it.mkdirs() }
            val image = File(dir, "$now.jpg")
            FileOutputStream(image).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            MediaScannerConnection.scanFile(context, arrayOf(image.absolutePath), null, null)
        }
    }

}