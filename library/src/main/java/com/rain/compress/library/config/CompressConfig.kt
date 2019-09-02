package com.rain.compress.library.config

import com.rain.compress.library.CompressConstant

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: CompressConfig.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-08-30 17:27
 *
 * Changes (from 2019-08-30)
 * -----------------------------------------------------------------
 * 2019-08-30 : Create CompressConfig.java (rxy);
 * -----------------------------------------------------------------
 */
class CompressConfig private constructor(builder: Builder) {

    private constructor() : this(Builder())

    /**
     * 不压缩的最小像素
     * */
    var unCompressMinPixel = 1000

    /**
     * 不压缩的标准像素
     * */
    var unCompressNormalPixel = 2000

    /**
     * 长或宽不超过的最大像素,单位px
     * */
    var maxPixel = 1200

    /**
     * 压缩到的最小大小,单位B
     * */
    var minSize = 200 * 1024

    /**
     * 是否采用像素压缩
     * */
    var enablePixelCompress = false

    /**
     * 是否采用质量压缩
     * */
    var enableQualityCompress = false

    /**
     * 压缩质量
     * */
    var quality = 80

    /**
     * 是否保留源文件
     * */
    var keepOriginalFile = true

    /**
     * 压缩的宽度
     * */
    var reqWidth: Int = 480

    /**
     * 压缩的高度
     * */
    var reqHeight: Int = 800

    /**
     * 压缩后保存图片的路径
     * */
    var imageDir: String = CompressConstant.COMPRESS_CACHE

    init {
        unCompressMinPixel = builder.unCompressMinPixel
        unCompressNormalPixel = builder.unCompressNormalPixel
        maxPixel = builder.maxPixel
        minSize = builder.minSize
        enablePixelCompress = builder.enablePixelCompress
        enableQualityCompress = builder.enableQualityCompress
        quality = builder.quality
        keepOriginalFile = builder.keepOriginalFile
        reqWidth = builder.reqWidth
        reqHeight = builder.reqHeight
        imageDir = builder.imageDir
    }


    companion object {
        fun getDefaultConfig() = CompressConfig()
    }

    class Builder {

        internal var unCompressMinPixel = 1000
        internal var unCompressNormalPixel = 2000
        internal var maxPixel = 1200
        internal var minSize = 200 * 1024
        internal var enablePixelCompress = false
        internal var enableQualityCompress = false
        internal var quality = 80
        internal var keepOriginalFile = true
        internal var reqWidth = 480
        internal var reqHeight = 800
        internal var imageDir = CompressConstant.COMPRESS_CACHE

        fun unCompressMinPixel(compressMinPixel: Int): Builder {
            unCompressMinPixel = compressMinPixel
            return this
        }

        fun unCompressNormalPixel(compressNormalPixel: Int): Builder {
            unCompressNormalPixel = compressNormalPixel
            return this
        }

        fun maxPixel(maxPixel: Int): Builder {
            this.maxPixel = maxPixel
            return this
        }

        fun minSize(minSize: Int): Builder {
            this.minSize = minSize
            return this
        }

        fun enablePixelCompress(enablePixelCompress: Boolean): Builder {
            this.enablePixelCompress = enablePixelCompress
            return this
        }


        fun enableQualityCompress(enableQualityCompress: Boolean): Builder {
            this.enableQualityCompress = enableQualityCompress
            return this
        }

        fun quality(quality: Int): Builder {
            this.quality = quality
            return this
        }

        fun keepOriginalFile(keepOriginalFile: Boolean): Builder {
            this.keepOriginalFile = keepOriginalFile
            return this
        }

        fun reqWidth(width: Int): Builder {
            this.reqWidth = width
            return this
        }

        fun reqHeight(height: Int): Builder {
            this.reqHeight = height
            return this
        }

        fun imageDir(dir: String): Builder {
            this.imageDir = dir
            return this
        }

        fun build(): CompressConfig {
            return CompressConfig(this)
        }
    }
}