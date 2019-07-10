package com.app.green_spoon

import android.net.Uri

interface OnVideoPickListener{
    fun onVideoCaptureSuccess(mUri : Uri)
    fun onVideoPickSuccess(mUri : Uri,path : String?)
    fun onVideoPickFailed()
}