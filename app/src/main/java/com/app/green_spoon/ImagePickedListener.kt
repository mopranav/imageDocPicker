package com.app.green_spoon

import android.net.Uri

interface ImagePickedListener{
    fun onCaptureSuccess(mUri : Uri)
    fun onImagePickSuccess(mUri : Uri,path : String?)
    fun onImagePickFailed()
}