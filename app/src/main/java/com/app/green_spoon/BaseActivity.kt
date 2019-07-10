//package com.app.green_spoon
//
//import android.content.pm.PackageManager
//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
//
//open class BaseActivity : AppCompatActivity() {
//    lateinit var mOnPermissionListener: OnPermissionListener
//
//    fun requestAppPermissions(requestedPermissions: Array<String>,
//                              requestCode: Int, listener: OnPermissionListener) {
//        this.mOnPermissionListener = listener
//        var permissionCheck = PackageManager.PERMISSION_GRANTED
//        for (permission in requestedPermissions) {
//            permissionCheck += ContextCompat.checkSelfPermission(this, permission)
//        }
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, requestedPermissions, requestCode)
//        } else {
//            if (mOnPermissionListener != null) mOnPermissionListener.onPermissionGranted(requestCode)
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        for (permission in permissions) {
//            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
//                if (mOnPermissionListener != null) mOnPermissionListener.onPermissionGranted(requestCode)
//                break
//            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                if (mOnPermissionListener != null) mOnPermissionListener.onPermissionDenied(requestCode)
//                break
//            } else {
//                if (mOnPermissionListener != null)
//                    mOnPermissionListener.onPermissionNeverAsk(requestCode)
//                break
//            }
//        }
//    }
//
//}
