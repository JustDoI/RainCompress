package com.rain.compress.library.manager

import android.annotation.SuppressLint
import android.content.Context
import com.rain.compress.library.callback.CompressCallback
import com.rain.compress.library.callback.CompressImageCallback
import com.rain.compress.library.config.CompressConfig
import com.rain.compress.library.model.PhotoModel
import com.rain.compress.library.util.CompressUtils
import com.rain.compress.library.util.ContextUtils
import java.io.File

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: CompressManager.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-08-30 17:58
 *
 * Changes (from 2019-08-30)
 * -----------------------------------------------------------------
 * 2019-08-30 : Create CompressManager.java (rxy);
 * -----------------------------------------------------------------
 */
@SuppressLint("StaticFieldLeak")
object CompressManager : CompressImage {

    private var mConfig: CompressConfig? = null
    private var mImages: ArrayList<PhotoModel>? = null
    private var mCallback: CompressCallback? = null

    fun build(
        context: Context,
        config: CompressConfig,
        images: ArrayList<PhotoModel>,
        callback: CompressCallback
    ): CompressImage {
        mConfig = config
        ContextUtils.context = context.applicationContext
        mImages = images
        mCallback = callback
        return this
    }

    override fun compressImage() {
        if (mImages == null || mImages!!.isEmpty()) {
            mCallback!!.onCompressFailure("要压缩的图片集合为空")
            return
        }

        compressImage(mImages!![0], 0)
    }

    private fun compressImage(model: PhotoModel, index: Int) {
        if (!model.isCompressed) {
            if (model.originalPath.isNullOrEmpty()) {
                mImages!![index].isCompressed = false
                mImages!![index].errorMsg = "图片的原始路径为空"
                compressNext(index)
                return
            }

            //文件不存在或者不是文件
            val file = File(model.originalPath!!)
            if (!file.exists() || !file.isFile) {
                mImages!![index].isCompressed = false
                mImages!![index].errorMsg = "文件不存在或者不是文件"
                compressNext(index)
                return
            }

            //文件小于最小的压缩值
            if (file.length() < mConfig!!.minSize) {
                mImages!![index].isCompressed = true
                compressNext(index)
                return
            }

            //开始压缩
            CompressUtils.comPressImage(
                model.originalPath!!,
                mConfig!!,
                object : CompressImageCallback {
                    override fun onCompressSuccess(imagePath: String) {
                        mImages!![index].isCompressed = true
                        mImages!![index].compressPath = imagePath
                        compressNext(index)
                    }

                    override fun onCompressFailure(imagePath: String, errorMsg: String) {
                        mImages!![index].isCompressed = false
                        mImages!![index].errorMsg = errorMsg
                        compressNext(index)
                    }
                })
        }
    }

    private fun compressNext(index: Int) {
        var currentIndex = index
        if (currentIndex == mImages!!.size - 1) {
            //已经是最后一张
            var success = true
            mImages!!.forEach {
                if (!it.isCompressed) {
                    success = it.isCompressed
                }
            }
            if (success) {
                mCallback!!.onCompressSuccess(mImages!!)
            } else {
                mCallback!!.onCompressFailure(mImages!!)
            }
        } else {
            //不是最后一张，继续压缩下一张
            currentIndex += 1
            compressImage(mImages!![currentIndex], currentIndex)
        }
    }
}