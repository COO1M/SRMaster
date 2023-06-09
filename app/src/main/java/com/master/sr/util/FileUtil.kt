package com.master.sr.util

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.core.graphics.scale
import com.master.sr.app.App
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sqrt

object FileUtil {

    fun checkModel(directory: String, name: String, extension: String): String {
        val ctx = App.ctx
        val folder = File("${ctx.filesDir}/$directory")
        val file = File(folder, "$name.$extension")
        if (!file.exists()) {
            if (folder.exists()) {
                folder.walk().filter { it.extension == extension }.forEach { it.delete() }
            } else {
                folder.mkdirs()
            }
            ctx.assets.open("$directory-$name-$extension").copyTo(FileOutputStream(file))
        }

        //Delete V1.0 Model (a function to manage model cache, later...
        ctx.filesDir.walk()
            .maxDepth(1)
            .filter { it.isFile && it.extension == "ptl" && it.startsWith("realesr-") }
            .forEach { it.delete() }

        return file.absolutePath
    }

    fun uri2bitmap(uri: Uri, compress: Boolean): Bitmap {
        val source = ImageDecoder.createSource(App.ctx.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGB_565, true)
        if (compress) {
            val width = bitmap.width
            val height = bitmap.height
            val pixelCount = width * height
            if (pixelCount > 1000000) {
                val scaleFactor = sqrt(pixelCount / 1000000.0)
                return bitmap.scale((width / scaleFactor).toInt(), (height / scaleFactor).toInt())
            }
        }
        return bitmap
    }

    fun saveBitmap(bitmap: Bitmap) {
        val now = System.currentTimeMillis()
        val resolver = App.ctx.contentResolver
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
    }

}