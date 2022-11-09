package com.master.sr.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.graphics.scale
import com.master.sr.app.App
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sqrt

object FileUtil {

    @Suppress("DEPRECATION")
    fun uri2bitmap(uri: Uri, compress: Boolean): Bitmap {
        val context = App.ctx
        val bitmap = if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
        if (compress) {
            val width = bitmap.width
            val height = bitmap.height
            val pixelCount = width * height
            if (pixelCount > 500000) {
                val scaleFactor = sqrt(pixelCount / 500000.0)
                return bitmap.scale((width / scaleFactor).toInt(), (height / scaleFactor).toInt())
            }
        }
        return bitmap
    }

    fun saveBitmap(bitmap: Bitmap) {
        val context: Context = App.ctx
        val now = System.currentTimeMillis()
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