package com.master.sr.util

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.MemoryFormat
import org.pytorch.torchvision.TensorImageUtils
import kotlin.math.roundToInt

object TorchUtil {

    private val realesrganModel by lazy {
        val path = FileUtil.checkModel("realesrgan", "20230608182955", "ptl")
        LiteModuleLoader.load(path)
    }

    fun runRealesrgan(inBitmap: Bitmap): Bitmap {
        val inTensor = TensorImageUtils.bitmapToFloat32Tensor(
            inBitmap,
            floatArrayOf(0f, 0f, 0f),
            floatArrayOf(1f, 1f, 1f),
            MemoryFormat.CONTIGUOUS
        )

        val outTensor = realesrganModel.forward(IValue.from(inTensor)).toTensor()

        val outArray = outTensor.dataAsFloatArray

        val conversion = { f: Float -> ((f.coerceIn(0f, 1f)) * 255f).roundToInt() }
        val outWidth = inBitmap.width * 4
        val outHeight = inBitmap.height * 4
        val outCount = outWidth * outHeight
        val outPixels = IntArray(outCount)

        for (i in 0 until outCount) {
            val r = conversion(outArray[i])
            val g = conversion(outArray[i + outCount])
            val b = conversion(outArray[i + outCount * 2])
            outPixels[i] = Color.rgb(r, g, b)
        }

        val outBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565)
        outBitmap.setPixels(outPixels, 0, outWidth, 0, 0, outWidth, outHeight)

        return outBitmap
    }

    fun testRealesrgan(inBitmap: Bitmap): Bitmap {
        Log.d("TAG", "inTensor")
        val inTensor = TensorImageUtils.bitmapToFloat32Tensor(
            inBitmap,
            floatArrayOf(0f, 0f, 0f),
            floatArrayOf(1f, 1f, 1f),
            MemoryFormat.CONTIGUOUS
        )
        Log.d("TAG", "inTensor")

        Log.d("TAG", "outTensor")
        val outTensor = realesrganModel.forward(IValue.from(inTensor)).toTensor()
        Log.d("TAG", "outTensor")

        Log.d("TAG", "outArray")
        val outArray = outTensor.dataAsFloatArray
        Log.d("TAG", "outArray")

        val conversion = { f: Float -> ((f.coerceIn(0f, 1f)) * 255f).roundToInt() }
        val outWidth = inBitmap.width * 4
        val outHeight = inBitmap.height * 4
        val outCount = outWidth * outHeight
        val outPixels = IntArray(outCount)

        Log.d("TAG", "outPixels")
        for (i in 0 until outCount) {
            val r = conversion(outArray[i])
            val g = conversion(outArray[i + outCount])
            val b = conversion(outArray[i + outCount * 2])
            outPixels[i] = Color.rgb(r, g, b)
        }
        Log.d("TAG", "outPixels")

        Log.d("TAG", "outBitmap")
        val outBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565)
        outBitmap.setPixels(outPixels, 0, outWidth, 0, 0, outWidth, outHeight)
        Log.d("TAG", "outBitmap")

        return outBitmap
    }

}