package com.rain.compress.library.model

import java.io.Serializable

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: PhotoModel.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-08-30 15:18
 *
 * Changes (from 2019-08-30)
 * -----------------------------------------------------------------
 * 2019-08-30 : Create PhotoModel.java (rxy);
 * -----------------------------------------------------------------
 */
data class PhotoModel(
    var originalPath: String?,//原始路径
    var compressPath: String = "",//压缩后的路径
    var isCompressed: Boolean,//是否已压缩过
    var errorMsg: String?//压缩失败的错误信息
) : Serializable