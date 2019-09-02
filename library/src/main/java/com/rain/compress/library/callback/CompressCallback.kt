package com.rain.compress.library.callback

import com.rain.compress.library.model.PhotoModel

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: CompressCallback.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-08-30 15:15
 *
 * Changes (from 2019-08-30)
 * -----------------------------------------------------------------
 * 2019-08-30 : Create CompressCallback.java (rxy);
 * -----------------------------------------------------------------
 */
interface CompressCallback {

    fun onCompressSuccess(successPhoto: ArrayList<PhotoModel>)

    fun onCompressFailure(errorMsg: String)

    fun onCompressFailure(failurePhoto: ArrayList<PhotoModel>)
}