package com.example.kotlindemogithubproject.util

import android.content.Context
import android.os.Environment
import android.util.Base64
import androidx.annotation.NonNull
import androidx.annotation.Nullable

import java.io.File
import java.io.FileInputStream


object FileUtil {

    private val AUDIO_CACHE_DIR_NAME = "audio"

    private val SIGN_IMAGE_CACHE_DIR_NAME = "sign_image"

    private val HTTP_CACHE_DIR_NAME = "http_response"

    val isExternalStorageEnable: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    @Nullable
    fun getCacheDir(@NonNull context: Context, @NonNull dirName: String): File {
        val rootDir = context.externalCacheDir
        val cacheFile = File(rootDir, dirName)
        if (!cacheFile.exists()) {
            cacheFile.mkdir()
        }
        return cacheFile
    }


    @Nullable
    fun getAudioCacheDir(@NonNull context: Context): File {
        return getCacheDir(context, AUDIO_CACHE_DIR_NAME)
    }

    @Nullable
    fun getSignImageCacheDir(@NonNull context: Context): File {
        return getCacheDir(context, SIGN_IMAGE_CACHE_DIR_NAME)
    }


    @Nullable
    fun getHttpImageCacheDir(@NonNull context: Context): File {
        return getCacheDir(context, HTTP_CACHE_DIR_NAME)
    }

    @Throws(Exception::class)
    fun encodeBase64File(@NonNull path: String): String {
        val file = File(path)
        val inputFile = FileInputStream(file)
        val buffer = ByteArray(file.length().toInt())
        inputFile.read(buffer)
        inputFile.close()
        return Base64.encodeToString(buffer, Base64.DEFAULT)
    }

}
