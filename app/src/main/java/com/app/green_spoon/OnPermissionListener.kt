package com.app.green_spoon

interface OnPermissionListener {
    fun onPermissionGranted(requestCode: Int)
    fun onPermissionDenied(requestCode: Int)
    fun onPermissionNeverAsk(requestCode: Int)
}
