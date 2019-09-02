package com.rain.compress.demo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rain.compress.library.CompressConstant
import com.rain.compress.library.callback.CompressCallback
import com.rain.compress.library.config.CompressConfig
import com.rain.compress.library.manager.CompressManager
import com.rain.compress.library.model.PhotoModel
import com.rain.compress.library.util.FileUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var compressConfig = CompressConfig.getDefaultConfig()

    private var mScreenWidth = 0
    private var mScreenHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDisplay()

        val cachePath = cacheDir.absolutePath
        val dirPath = getDir("spanner", Context.MODE_PRIVATE).absolutePath
        val filePath = filesDir.absolutePath
        val externalCachePath = externalCacheDir?.absolutePath
        val externalFilePath1 = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val externalFilePath2 = getExternalFilesDir(null)

        val files = ContextCompat.getExternalCacheDirs(this)
        files.forEach {
            Log.e("mylog", "this----path is ${it.absolutePath}")
        }

        Log.d("mylog", "cachePath is $cachePath")
        Log.d("mylog", "dirPath is $dirPath")
        Log.d("mylog", "filePath is $filePath")
        Log.d("mylog", "externalCachePath is $externalCachePath")
        Log.d("mylog", "externalFilePath1 is $externalFilePath1")
        Log.d("mylog", "externalFilePath2 is $externalFilePath2")

        album.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
                } else {
                    openAlbum()
                }
            }
        }

        compressConfig = CompressConfig.Builder()
            .imageDir(externalCachePath!! + File.separator + CompressConstant.COMPRESS_CACHE)
            .reqWidth(mScreenWidth)
            .reqHeight(mScreenHeight)
            .keepOriginalFile(false)
            .build()

//        val config = CompressConfig.getDefaultConfig()
    }

    private fun initDisplay() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        mScreenWidth = metrics.widthPixels
        mScreenHeight = metrics.heightPixels
    }

    private fun comPressImage(images: ArrayList<PhotoModel>) {
        CompressManager.build(this, compressConfig, images, object : CompressCallback {
            override fun onCompressSuccess(successPhoto: ArrayList<PhotoModel>) {
                successPhoto.forEach {
                    Log.e("mylog", "压缩成功后的路径是:${it.compressPath}")
                }
            }

            override fun onCompressFailure(errorMsg: String) {
                Log.e("mylog", "压缩失败哦")
            }

            override fun onCompressFailure(failurePhoto: ArrayList<PhotoModel>) {
                failurePhoto.forEach {
                    Log.e("mylog", "压缩失败的原因是:${it.errorMsg}")
                }
            }
        }).compressImage()

    }

    /**
     * 打开相册
     * */
    private fun openAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 102 && data != null) {
                val uri = data.data
                if (uri != null) {
                    val path = FileUtils.formatUrl(this, uri)
                    Log.e("mylog", "图片路径是 $path")

                    val images = arrayListOf(
                        PhotoModel(path, "", false, null)
                    )
                    comPressImage(images)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty()) {
            var result = true
            grantResults.forEach {
                result = it == PackageManager.PERMISSION_GRANTED
            }
            if (result) {
                openAlbum()
            }
        }
    }
}
