package com.rain.compress.library.callback

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2014-2019, by your 爱空间, All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: CompressImageCallback.java
 * Author: rxy
 * Version: V100R001C01
 * Create: 2019-09-02 11:11
 *
 * Changes (from 2019-09-02)
 * -----------------------------------------------------------------
 * 2019-09-02 : Create CompressImageCallback.java (rxy);
 * -----------------------------------------------------------------
 */interface CompressImageCallback {

    fun onCompressSuccess(imagePath: String)

    fun onCompressFailure(imagePath: String, errorMsg: String)
}