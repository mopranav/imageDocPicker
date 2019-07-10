//package com.app.green_spoon
//
//import android.Manifest
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//
//class SampleActivity : AppCompatActivity(), ImagePickedListener, OnVideoPickListener {
//    lateinit var mGreenSpoonPickerImage : GreenSpoonPicker
//    lateinit var mGreenSpoonPickerVideo : GreenSpoonVideoPicker
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sample)
//
//        mGreenSpoonPickerImage = GreenSpoonPicker(this@SampleActivity,
//                false,
//                GreenSpoonPicker.CODE_DIALOG,
//                this@SampleActivity,"files/")
//
//        mGreenSpoonPickerVideo = GreenSpoonVideoPicker(this@SampleActivity,
//                false,
//                GreenSpoonPicker.CODE_DIALOG,
//                this@SampleActivity,"files/")
//
//    }
//
//    fun getImage(view : View) {
//        mGreenSpoonPickerImage.setStoragePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        mGreenSpoonPickerImage.setCameraPermission(Manifest.permission.CAMERA)
//        mGreenSpoonPickerImage.setMessageForCaptureDialog("Lol No Cam")
//        mGreenSpoonPickerImage.setMessageForGalleryDialog("Lol No Gallery")
//        mGreenSpoonPickerImage.setMessageForNeveraskCameraDialog("Camera permission denied")
//        mGreenSpoonPickerImage.setMessageForNeveraskGalleryDialog("Gallery permission denied")
//        mGreenSpoonPickerImage.setApptitle("Permission demo")
//        mGreenSpoonPickerImage.start()
//    }
//
//    fun getVideo(view :View){
//        mGreenSpoonPickerVideo.setStoragePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        mGreenSpoonPickerVideo.setCameraPermission(Manifest.permission.CAMERA)
//        mGreenSpoonPickerVideo.setMessageForCaptureDialog("Lol No Cam")
//        mGreenSpoonPickerVideo.setMessageForGalleryDialog("Lol No Gallery")
//        mGreenSpoonPickerVideo.setMessageForNeveraskCameraDialog("Camera permission denied")
//        mGreenSpoonPickerVideo.setMessageForNeveraskGalleryDialog("Gallery permission denied")
//        mGreenSpoonPickerVideo.setApptitle("Permission demo")
//        mGreenSpoonPickerVideo.start()
//    }
//
//    override fun onCaptureSuccess(mUri: Uri) {
//        Log.e("Image uri",""+mUri);
//    }
//
//    override fun onImagePickFailed() {}
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        mGreenSpoonPickerImage.onActivityResult(requestCode, resultCode, data)
//        mGreenSpoonPickerVideo.onActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        mGreenSpoonPickerImage.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        mGreenSpoonPickerVideo.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    override fun onVideoCaptureSuccess(mUri: Uri) {
//        Log.e("Video uri",""+mUri);
//    }
//
//    override fun onVideoPickSuccess(mUri: Uri, path: String?) {
//        Log.e("**** ",mUri.toString())
//        Log.e("**** ",path!!.toString())
//    }
//
//    override fun onVideoPickFailed() {
//    }
//
//    override fun onImagePickSuccess(mUri: Uri, path: String?) {
//        Log.e("**** ",mUri.toString())
//        Log.e("**** ",path!!.toString())
//    }
//}