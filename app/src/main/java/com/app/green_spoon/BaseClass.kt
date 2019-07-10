package com.app.green_spoon

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

open class BaseClass{
    lateinit var mOnPermissionListener: OnPermissionListener

    fun requestAppPermissions(requestedPermissions: Array<String>,
                              requestCode: Int, listener: OnPermissionListener,mActivity: AppCompatActivity) {
        this.mOnPermissionListener = listener
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permission in requestedPermissions) {
            permissionCheck += ContextCompat.checkSelfPermission(mActivity, permission)
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, requestedPermissions, requestCode)
        } else {
            if (mOnPermissionListener != null) mOnPermissionListener.onPermissionGranted(requestCode)
        }
    }



}
