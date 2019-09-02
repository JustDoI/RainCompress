package com.rain.compress.library.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import com.rain.compress.library.CompressConstant
import com.rain.compress.library.callback.CompressImageCallback
import com.rain.compress.library.config.CompressConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: CompressUtils.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-08-30 15:22
 *
 * Changes (from 2019-08-30)
 * -----------------------------------------------------------------
 * 2019-08-30 : Create CompressUtils.java (rxy);
 * -----------------------------------------------------------------
 */object CompressUtils {

    /**
     * 压缩图片
     * @param filePath 文件途径
     * @param quality 压缩质量
     * @param reqWidth 要求的宽度
     * @param reqHeight 要求的高度
     * */
    fun getSmallBitmap(
        filePath: String,
        quality: Int,
        reqWidth: Int = 480,
        reqHeight: Int = 800
    ): Bitmap? {
        val options = BitmapFactory.Options()
        //设置为true表示不把bitmap加载到内存中去
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        //计算采样，主流手机是800*480
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(filePath, options) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        byteArrayOutputStream.close()
        return bitmap
    }

    /**
     * 计算缩放率
     * @param options 配置信息
     * @param reqWidth 要求的宽度
     * @param reqHeight 要求的高度
     * */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val width = options.outWidth
        val height = options.outHeight
        var inSampleSize = 1
        if (width > reqWidth || height > reqHeight) {
            val widthRadio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            val heightRadio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            inSampleSize = if (widthRadio > heightRadio) widthRadio else heightRadio
        }
        return inSampleSize
    }

    /**
     * 质量压缩
     * @param bitmap
     * @param imageSize 文件大小的最高限制
     * @param quality 压缩质量
     * */
    fun compressBitmap(bitmap: Bitmap, imageSize: Int, quality: Int = 100): Bitmap? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        var option = quality
        while (byteArrayOutputStream.toByteArray().size / 1024 > imageSize) {
            option -= 10
            byteArrayOutputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        }
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        return BitmapFactory.decodeStream(byteArrayInputStream, null, null)
    }

    fun comPressImage(filePath: String, config: CompressConfig, listener: CompressImageCallback) {
        val options = BitmapFactory.Options()
        //设置为true表示不把bitmap加载到内存中去
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        //计算采样，主流手机是800*480
        options.inSampleSize = calculateInSampleSize(options, config.reqWidth, config.reqHeight)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(filePath, options)

        if (bitmap == null) {
            listener.onCompressFailure(filePath, "文件解码失败")
            return
        }

        try {
            val compressPath = if (config.imageDir == CompressConstant.COMPRESS_CACHE)
                ContextCompat.getExternalCacheDirs(ContextUtils.context!!)[0].absolutePath + File.separator + CompressConstant.COMPRESS_CACHE
            else
                config.imageDir
            val file = File(compressPath + File.separator + getImageName() + ".jpeg")
            if (!file.exists())
                file.createNewFile()

            val fileOutStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, config.quality, fileOutStream)
            fileOutStream.flush()
            fileOutStream.close()

            if (!config.keepOriginalFile) {
                val originalFile = File(filePath)
                originalFile.deleteOnExit()
            }
            listener.onCompressSuccess(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            listener.onCompressFailure(filePath, "图片压缩失败")
        }
    }

    private fun getImageName(): String {
        val now = GregorianCalendar()
        val simpleDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return simpleDate.format(now.time)
    }
}