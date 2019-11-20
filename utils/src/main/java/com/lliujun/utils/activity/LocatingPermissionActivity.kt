package com.lliujun.utils.activity

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lliujun.utils.extensions.android.hasPermission
import com.lliujun.utils.util.isLocationServiceEnabled

/**
 * 封装了定位权限请求, 请求逻辑为:
 * 1. 首先判断是否开启了定位服务,如果没有开启,则提示用户去设置界面开启定位服务:
 *      开启成功之后进入下一步, 否则回调 `onPermissionDenied` 并返回
 *
 * 2. 判断用户是否同意了定位权限的请求,如果没有,则动态申请权限:
 *      申请成功之后进入下一步,否则回调 `onPermissionDenied` 并返回
 *
 * 3. 判断用户是否需要判断蓝牙是否开启:
 *
 *    3.1 如果不需要,则回调 `onPermissionGranted` 并返回
 *
 *    3.2 如果需要,则继续判断蓝牙是否可用: 不可用,则回调 `onPermissionDenied`.
 *        再判断是否蓝牙是否开启,如果没有开启,则提示用户去开启蓝牙:
 *        开启之后回调 `onPermissionGranted`,
 *        否则回调 `onPermissionDenied`
 * */
abstract class LocatingPermissionActivity : AppCompatActivity(), IDialog {

    private val requestPermission = Manifest.permission.ACCESS_FINE_LOCATION

    private var resultListener: Result? = null
    private var isCheckBluetoothEnabled = false

    @Suppress("unused")
    protected fun checkAllPermission(listener: Result) {
        checkAllPermission(false, listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("LocatingPermission", "onCreate")
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun checkAllPermission(isCheckBluetoothEnabled: Boolean, listener: Result) {
        this.resultListener = listener
        this.isCheckBluetoothEnabled = isCheckBluetoothEnabled

        if (isLocationServiceEnabled()) {
            checkLocationPermission()
        } else {
            showDialog(
                IDialog.Config(
                    "定位服务没有开启,程序无法正常运行,是否跳转到定位设置界面以打开定位服务",
                    "去打开", "退出"
                ), {
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        START_LOCATION_SETTING_CODE
                    )
                }
            )
        }
    }

    private fun checkLocationPermission() {
        if (hasPermission(requestPermission)
        ) {
            if (!isCheckBluetoothEnabled()) {
                resultListener?.onPermissionGranted()
                return
            }
            // 判断蓝牙是否开启
            checkBluetoothEnabled()
        } else {
            requestPermissionWithDialog(
                "需要用户当前的位置信息,以查询附近的停车场信息",
                arrayOf(requestPermission),
                PERMISSION_LOCATION_REQUEST_CODE
            )
        }
    }


    @Suppress("SameParameterValue")
    private fun requestPermissionWithDialog(
        message: String,
        permissions: Array<String>,
        code: Int
    ) {
        showDialog(
            IDialog.Config(message,
                "去设置",
                "退出",
                false),{
                ActivityCompat.requestPermissions(this, permissions, code)
            }
        )
    }

    /**
     * 检查蓝牙是否处于开启状态
     * */
    private fun checkBluetoothEnabled() {
        if (!isBluetoothAvailable()) {
            showDialog(
                IDialog.Config("去开启蓝牙",
                    "开启",
                    "退出",
                    false),{
                    startActivityForResult(
                        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                        START_BLUETOOTH_ENABLE_SETTING_CODE
                    )
                },{
                    finishWithError("蓝牙未开启,程序无法正常运行")
                }
            )
        } else {
            resultListener?.onPermissionGranted()
        }
    }

    @SuppressLint("MissingPermission")
    private fun isBluetoothAvailable(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            resultListener?.onPermissionDenied(ERROR_CODE_DEVICE_DO_NOT_SUPPORT_BLUETOOTH)
            return false
        }
        return bluetoothAdapter.isEnabled
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            START_LOCATION_SETTING_CODE -> {
                if (isLocationServiceEnabled()) {
                    checkLocationPermission()
                } else {
                    resultListener?.onPermissionDenied(ERROR_CODE_LOCATING_SERVICE_IS_NOT_ENABLE)
                }
            }
            PERMISSION_LOCATION_REQUEST_CODE -> {
                if (hasPermission(requestPermission)
                ) {
                    if (!isCheckBluetoothEnabled()) {
                        resultListener?.onPermissionGranted()
                        return
                    }
                    checkBluetoothEnabled()
                } else {
                    resultListener?.onPermissionDenied(ERROR_CODE_LOCATING_PERMISSION_IS_DENIED)
                }
            }
            START_BLUETOOTH_ENABLE_SETTING_CODE -> {
                if (isBluetoothAvailable()) {
                    resultListener?.onPermissionGranted()
                } else {
                    resultListener?.onPermissionDenied(ERROR_CODE_BLUETOOTH_IS_NOT_ENABLED)
                }
            }
        }
    }

    /**
     * 是否需要检测蓝牙是否开启
     * */
    open fun isCheckBluetoothEnabled(): Boolean {
        return this.isCheckBluetoothEnabled
    }

    private fun finishWithError(message: String) {
        showDialog(
            IDialog.Config(message,
                "开启",
                "",
                false),{
                finish()
            },{
                finishWithError("蓝牙未开启,程序无法正常运行")
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_LOCATION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionsGranted()
                } else {
                    onPermissionsDenied()
                }
            }
        }
    }

    private fun onPermissionsGranted() {
        if (!isCheckBluetoothEnabled()) {
            resultListener?.onPermissionGranted()
            return
        }
        checkBluetoothEnabled()
    }

    private fun onPermissionsDenied() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, requestPermission)
        ) {
            showDialog(
                IDialog.Config("没有位置权限程序将无法正常运行,跳转到设置界面并允许app访问位置权限?",
                    "去设置",
                    "退出"),
                {
                    startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }, PERMISSION_LOCATION_REQUEST_CODE)
                },
                {
                    finish()
                }
            )
        }
    }

    @Suppress("unused")
    open inner class ResultHandler : Result {
        override fun onPermissionDenied(errorCode: Int) {

            val errorMessage = when (errorCode) {
                ERROR_CODE_DEVICE_DO_NOT_SUPPORT_BLUETOOTH -> "设备不支持蓝牙"
                ERROR_CODE_BLUETOOTH_IS_NOT_ENABLED -> "蓝牙没有开启"
                ERROR_CODE_LOCATING_PERMISSION_IS_DENIED -> "定位权限请求失败"
                ERROR_CODE_LOCATING_SERVICE_IS_NOT_ENABLE -> "定位服务没有开启"
                else -> ""
            }
            finishWithError("$errorMessage,程序无法正常运行")
        }

        override fun onPermissionGranted() {

        }
    }

    interface Result {
        /**
         * 定位权限申请成功,满足下列所有条件即判断成功
         * 1. 定位服务开启
         * 2. 用户同意了定位权限
         * 3. 如果需要检测蓝牙(isCheckBluetoothEnabled = true),则蓝牙是可以用的,并且处于打开状态
         * */
        fun onPermissionGranted()

        /**
         * 存在某一个权限没有申请成功, 满足下列条件之一,即会判断失败
         * 1. 定位服务没有开启
         * 2. 用户决绝了定位权限
         * 3. 如果需要检测蓝牙
         *    3.1 设备不支持蓝牙
         *    3.2 蓝牙处于关闭状态
         * */
        fun onPermissionDenied(errorCode: Int)
    }


    companion object {

        private const val START_LOCATION_SETTING_CODE = 1000
        private const val START_BLUETOOTH_ENABLE_SETTING_CODE = 1001
        private const val PERMISSION_LOCATION_REQUEST_CODE = 1002

        /**
         * 定位服务没有开启
         * */
        private const val ERROR_CODE_LOCATING_SERVICE_IS_NOT_ENABLE = 1

        /**
         * 用户拒绝了定位权限的请求
         * */
        private const val ERROR_CODE_LOCATING_PERMISSION_IS_DENIED = 2


        /**
         * 蓝牙服务没有开启
         * */
        private const val ERROR_CODE_BLUETOOTH_IS_NOT_ENABLED = 3

        /**
         * 设备不支持蓝牙功能
         * */
        private const val ERROR_CODE_DEVICE_DO_NOT_SUPPORT_BLUETOOTH = 4
    }
}
