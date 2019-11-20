package com.lliujun.utils.extensions.android

import android.content.Context
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @param isPublic true 表示公开文件夹,false 表示创建的文件是仅对该应用可见的
 * @param directory 文件存储的目录, null 表示自己常见目录
 * */
@Throws(IOException::class)
private fun createImageFile(context: Context, isPublic: Boolean = true, directory: String? = null): File? {

    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = String.format("JPEG_%s.jpg", timeStamp)

    var storageDir: File?
    if (isPublic) {
        storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) storageDir.mkdirs()
    } else {
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    if (directory != null) {
        storageDir = File(storageDir, directory)
        if (!storageDir.exists()) storageDir.mkdirs()
    }

    // Avoid joining path components manually
    val tempFile = File(storageDir, imageFileName)
    // Handle the situation that user's external storage is not ready
    return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
        null
    } else tempFile
}