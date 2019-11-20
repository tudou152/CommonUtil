package com.lliujun.utils.extensions.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/**
 * 拍照
 */
fun Activity.startCamera(output: Uri, requestCode: Int) {
    // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
    val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // 设置拍照所得照片的输出目录
    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
    startActivityForResult(takePhotoIntent, requestCode)
}

fun Fragment.startCamera(output: Uri, requestCode: Int) {
    // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
    val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    // 设置拍照所得照片的输出目录
    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
    startActivityForResult(takePhotoIntent, requestCode)
}

/**
 * 从相册选取
 */
fun Activity.chooseFromAlbum(mimeType: String, requestCode: Int) {
    val choiceFromAlbumIntent = Intent(Intent.ACTION_GET_CONTENT)
    // 设置数据类型为图片类型
    choiceFromAlbumIntent.type = mimeType
    startActivityForResult(choiceFromAlbumIntent, requestCode)
}

fun Activity.chooseImageFromAlbum(requestCode: Int) = chooseFromAlbum("image/*", requestCode)

fun Fragment.chooseImageFromAlbum(mimeType: String = "image/*", requestCode: Int) {
    val choiceFromAlbumIntent = Intent(Intent.ACTION_GET_CONTENT)
    // 设置数据类型为图片类型
    choiceFromAlbumIntent.type = mimeType
    startActivityForResult(choiceFromAlbumIntent, requestCode)
}

/**
 * 调用系统的裁剪图片的组件
 */
fun Activity.cropPhoto(inputUri: Uri, outputUri: Uri, requestCode: Int) {
    // 调用系统裁剪图片的 Action
    val cropPhotoIntent = Intent("com.android.camera.action.CROP")
    // 设置数据Uri 和类型
    cropPhotoIntent.setDataAndType(inputUri, "image/*")
    // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
    cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // 设置图片的最终输出目录
    cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
    startActivityForResult(cropPhotoIntent, requestCode)
}

fun Fragment.cropPhoto(inputUri: Uri, output: Uri, requestCode: Int) {
    // 调用系统裁剪图片的 Action
    val cropPhotoIntent = Intent("com.android.camera.action.CROP")
    // 设置数据Uri 和类型
    cropPhotoIntent.setDataAndType(inputUri, "image/*")
    // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
    cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // 设置图片的最终输出目录
    cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, output)
    startActivityForResult(cropPhotoIntent, requestCode)
}

fun File.toUri(context: Context, authority: String): Uri {
    /**
     * 因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常，
     * 因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类，
     * 我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说)
     */
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, authority, this)
    } else {
        Uri.fromFile(this) // Android 7.0 以前使用原来的方法来获取文件的 Uri
    }
}
