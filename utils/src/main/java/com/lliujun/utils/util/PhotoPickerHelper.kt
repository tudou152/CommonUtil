package com.lliujun.utils.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.core.os.EnvironmentCompat
import androidx.fragment.app.Fragment
import com.lliujun.utils.extensions.android.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能:
 * 1.拍照
 * 2.从相册选取照片
 *
 * 处理了权限请求
 *
 * @param authority The authority of a {@link FileProvider} defined in a
 *            {@code <provider>} element in your app's manifest.
 * @param result 获取结果的回调, errorMessage为null的时候表示获取图片失败, 否则成功
 * */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class PhotoPickerHelper(
    private val fragment: Fragment,
    private val authority: String,
    private val result: PhotoResultHandler
) {

    private var takePhotoUri: Uri? = null

    /**
     * 直接打开相册来获取图片
     * */
    fun getPhotoFromAlbum() {
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        with(fragment) {
            if (hasPermission(writePermission)) {
                chooseImageFromAlbum(requestCode = CHOOSE_PHOTO_CODE)
            } else {
                requestPermissions(
                    arrayOf(writePermission),
                    REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
                )
            }
        }
    }

    /**
     * 直接打开相机来获取图片
     * */
    fun getPhotoFromCamera() {
        val cameraPermission = Manifest.permission.CAMERA

        with(fragment) {
            if (hasPermission(cameraPermission)) {
                startCamera()
            } else {
                requestPermissions(arrayOf(cameraPermission), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    private fun startCamera() {
        val file = createImageFile(true) ?: return
        takePhotoUri = file.toUri(fragment.context!!, authority)

        fragment.startCamera(takePhotoUri!!, TAKE_PHOTO_REQUEST_CODE)
    }

    @Throws(IOException::class)
    private fun createImageFile(isPublic: Boolean = true, directory: String? = null): File? {

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = String.format("JPEG_%s.jpg", timeStamp)

        var storageDir: File?
        if (isPublic) {
            storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (!storageDir!!.exists()) storageDir.mkdirs()
        } else {
            storageDir = fragment.context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_OK) return
        val outputUri = createImageFile(true, "geoxing")?.let {
            Uri.fromFile(it)
        } ?: return

        when (requestCode) {
            CHOOSE_PHOTO_CODE -> data?.data?.let {
                fragment.cropPhoto(it, outputUri, CROP_PHOTO_REQUEST_CODE)
            }
            TAKE_PHOTO_REQUEST_CODE -> takePhotoUri?.let {
                fragment.cropPhoto(it, outputUri, CROP_PHOTO_REQUEST_CODE)
            }
            CROP_PHOTO_REQUEST_CODE -> {
                data?.action?.let { result(null, it) } ?: result("裁剪失败", null)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    @SuppressLint("MissingPermission")
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getPhotoFromAlbum()
            else
                result("没有权限无法打开相册", null)

        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCamera()
            else
                result("没有权限无法启动相机", null)
        }
    }

    companion object {

        private const val TAKE_PHOTO_REQUEST_CODE = 1001
        private const val CROP_PHOTO_REQUEST_CODE = 1002
        private const val CHOOSE_PHOTO_CODE = 1003

        private const val REQUEST_CAMERA_PERMISSION = 2002
        private const val REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 2003

        fun create(
            fragment: Fragment,
            authority: String,
            handler: PhotoResultHandler
        ): PhotoPickerHelper {
            return PhotoPickerHelper(fragment, authority, handler)
        }
    }
}

typealias PhotoResultHandler = (errorMessage: String?, result: String?) -> Unit
