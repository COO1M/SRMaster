package com.master.sr.util

import android.graphics.Bitmap
import android.graphics.Color
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

        val outShape = outTensor.shape()
        val outWidth = outShape[3].toInt()
        val outHeight = outShape[2].toInt()
        val outCount = outWidth * outHeight

        val conversion = { f: Float -> ((f.coerceIn(0f, 1f)) * 255f).roundToInt() }

        val outPixels = IntArray(outCount)
        val outBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565)
        for (i in 0 until outCount) {
            val r = conversion(outArray[i])
            val g = conversion(outArray[i + outCount])
            val b = conversion(outArray[i + outCount * 2])
            outPixels[i] = Color.rgb(r, g, b)
        }
        outBitmap.setPixels(outPixels, 0, outWidth, 0, 0, outWidth, outHeight)

        return outBitmap
    }

}