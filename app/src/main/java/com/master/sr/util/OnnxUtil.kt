package com.master.sr.util

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.extensions.OrtxPackage
import android.graphics.Bitmap
import android.graphics.Color
import java.util.Collections
import kotlin.math.roundToInt

object OnnxUtil {

    private val realesrganModel by lazy {
        val path = FileUtil.checkModel("realesrgan", "20230608184611", "onnx")
        val ortEnv = OrtEnvironment.getEnvironment()
        val opts = OrtSession.SessionOptions().apply {
            addNnapi()
            registerCustomOpLibrary(OrtxPackage.getLibraryPath())
        }
        ortEnv.createSession(path, opts)
    }

    fun runRealesrgan(inBitmap: Bitmap): Bitmap? {
        val ortEnv = OrtEnvironment.getEnvironment()
        val inTensor = OnnxTensor.createTensor(
            ortEnv,
            FileUtil.bitmap2floatBuffer(inBitmap),
            longArrayOf(1, 3, inBitmap.height.toLong(), inBitmap.width.toLong())
        )

        val outTensor = realesrganModel.run(Collections.singletonMap("input", inTensor))
        inTensor.close()

        val outArray = (outTensor.get(0).value as Array<*>)[0] as Array<Array<FloatArray>>
        outTensor.close()

        val conversion = { f: Float -> ((f.coerceIn(0f, 1f)) * 255f).roundToInt() }
        val outWidth = inBitmap.width * 4
        val outHeight = inBitmap.height * 4
        val outCount = outWidth * outHeight
        val outPixels = IntArray(outCount)

        for (i in 0 until outCount) {
            val y = i / outWidth
            val x = i % outWidth
            val r = conversion(outArray[0][y][x])
            val g = conversion(outArray[1][y][x])
            val b = conversion(outArray[2][y][x])
            outPixels[i] = Color.rgb(r, g, b)
        }

        val outBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.RGB_565)
        outBitmap.setPixels(outPixels, 0, outWidth, 0, 0, outWidth, outHeight)

        return outBitmap
    }
}