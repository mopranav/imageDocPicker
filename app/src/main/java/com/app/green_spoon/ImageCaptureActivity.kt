//package com.app.green_spoon
//
//import android.Manifest
//import android.app.Activity
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.os.StrictMode
//import android.provider.MediaStore
//import android.provider.Settings
//import android.support.v7.app.AlertDialog
//import android.util.Log
//import android.view.View
//import android.view.WindowManager
//import android.widget.TextView
//import com.theartofdev.edmodo.cropper.CropImage
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class ImageCaptureActivity : BaseActivity(), OnPermissionListener {
//
//    val CAMERA_PERMISSION = 2512
//    val STORAGE_PERMISSION = 6325
//
//    val SETTING_CAMERA_PERMISSION = 5454
//    val SETTING_GALLERY_PERMISSION = 5954
//
//    val REQUEST_IMAGE_CAPTURE = 6542
//    val REQUEST_GALLERY_IMAGE = 1234
//    val ONLY_STORAGE_PERMISSION = 4546
//
//    var alertDialog : AlertDialog? = null
//
//    //Conditions
//    var isForcefullyGetPermission = false
//
//    var isCropEnable: Boolean = false
//    var code: Int = 2
//
//    var CAPTURE_MESSAGE = ""
//    var GALLERY_MESSAGE = ""
//
//    private var mFileImage: File? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
//
//        isForcefullyGetPermission = intent.extras.getBoolean("isForce",false)
//        isCropEnable = intent.extras.getBoolean("crop",false)
//        code = intent.extras.getInt("code",2)
//        CAPTURE_MESSAGE = intent.extras.getString("cap_msg")
//        GALLERY_MESSAGE = intent.extras.getString("gal_msg")
//
//        if(code == GreenSpoonPicker.CODE_DIALOG){
//            openImagePickerDialog()
//        }
//        else if(code == GreenSpoonPicker.CODE_GALLERYIMAGE){
//            pickGalleryImage()
//        }
//        else if(code == GreenSpoonPicker.CODE_PICK_IMAGE){
//            captureImageForCamera()
//        }
//
//    }
//
//    fun captureFromCamera(view:View){
//        captureImageForCamera()
//    }
//
//    fun pickFromGallery(view:View){
//        pickGalleryImage()
//    }
//
//    fun openDialogForPicker(view:View){
//        openImagePickerDialog()
//    }
//
//    fun captureImageForCamera(){
//        checkForPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION,this@ImageCaptureActivity)
//    }
//
//    fun pickGalleryImage(){
//        checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,ONLY_STORAGE_PERMISSION,this@ImageCaptureActivity)
//    }
//
//    fun checkForPermission(requestedPermissions: String, code: Int, mOnPermissionListener: OnPermissionListener) {
//        requestAppPermissions(arrayOf(requestedPermissions), code,
//                object : OnPermissionListener {
//                    override fun onPermissionGranted(requestCode: Int) {
//                        Log.e("onPermissionGranted", "onPermissionGranted")
//                        mOnPermissionListener.onPermissionGranted(requestCode)
//                    }
//
//                    override fun onPermissionDenied(requestCode: Int) {
//                        Log.e("onPermissionDenied", "onPermissionDenied")
//                        mOnPermissionListener.onPermissionDenied(requestCode)
//                    }
//
//                    override fun onPermissionNeverAsk(requestCode: Int) {
//                        mOnPermissionListener.onPermissionNeverAsk(requestCode)
//                    }
//                })
//    }
//
//
//    override fun onPermissionGranted(requestCode: Int) {
//        if (requestCode == CAMERA_PERMISSION) {
//            checkForPermission(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    STORAGE_PERMISSION,
//                    this@ImageCaptureActivity
//            )
//        } else if (requestCode == STORAGE_PERMISSION) {
//            captureImageIntent()
//        }
//        else if(requestCode==ONLY_STORAGE_PERMISSION){
//            selectPictureFromGallery()
//        }
//    }
//    override fun onPermissionDenied(requestCode: Int) {
//        if (requestCode == CAMERA_PERMISSION) {
//            requestPermissionFromSettingsForCamera()
//        }
//        else if(requestCode == STORAGE_PERMISSION){
//            requestPermissionFromSettingsForCamera()
//        }
//        else if(requestCode==ONLY_STORAGE_PERMISSION){
//            requestPermissionFromSettingsForGallery()
//        }
//    }
//    override fun onPermissionNeverAsk(requestCode: Int) {
//        if(requestCode== ONLY_STORAGE_PERMISSION){
//            dialog_with_callbackCancelable(this@ImageCaptureActivity,
//                    "No Gallery Permission",
//                    object : View.OnClickListener {
//                        override fun onClick(v: View?) {
//                            startActivityForResult(Intent().apply {
//                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                data = Uri.fromParts("package", packageName, null)
//                            }, SETTING_GALLERY_PERMISSION)
//                        }
//                    })
//        }
//        else {
//            dialog_with_callbackCancelable(this@ImageCaptureActivity,
//                    "no cam or storage",
//                    object : View.OnClickListener {
//                        override fun onClick(v: View?) {
//                            startActivityForResult(Intent().apply {
//                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                data = Uri.fromParts("package", packageName, null)
//                            }, SETTING_CAMERA_PERMISSION)
//                        }
//                    })
//        }
//    }
//
//
//    fun captureImageIntent() {
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        mFileImage = createImageFile()
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileImage))
//        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
//    }
//
//    fun createImageFile(): File {
//        var image: File
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES
//        )
//        storageDir.mkdirs()
//
//        image = File.createTempFile(
//                imageFileName, /* prefix */
//                ".jpg", /* suffix */
//                storageDir      /* directory */
//        )
//
//        return image
//    }
//
//    fun requestPermissionFromSettingsForCamera() {
//        dialog_with_callbackCancelable(this@ImageCaptureActivity,
//                CAPTURE_MESSAGE,
//                object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        startActivityForResult(Intent().apply {
//                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            data = Uri.fromParts("package", packageName, null)
//                        }, SETTING_CAMERA_PERMISSION)
//                    }
//                })
//    }
//
//
//    fun requestPermissionFromSettingsForGallery() {
//        dialog_with_callbackCancelable(this@ImageCaptureActivity,
//                GALLERY_MESSAGE,
//                object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        startActivityForResult(Intent().apply {
//                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            data = Uri.fromParts("package", packageName, null)
//                        }, SETTING_GALLERY_PERMISSION)
//                    }
//                })
//    }
//
//    private fun selectPictureFromGallery() {
//        val photoPickerIntent = Intent(Intent.ACTION_PICK)
//        photoPickerIntent.type = "image/*"
//        startActivityForResult(photoPickerIntent, REQUEST_GALLERY_IMAGE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
//            if(resultCode == Activity.RESULT_OK) {
//                if (isCropEnable) {
//                    CropImage.activity(Uri.fromFile(mFileImage)).start(this@ImageCaptureActivity);
//                } else {
//                    if (GreenSpoonPicker.mImagePickedListener != null) {
//                        GreenSpoonPicker.mImagePickedListener!!.onImagePickSuccess(Uri.fromFile(mFileImage))
//                        finish()
//                    }
//                }
//            }
//            else{
//                if (GreenSpoonPicker.mImagePickedListener != null) {
//                    GreenSpoonPicker.mImagePickedListener!!.onImagePickFailed()
//                    finish()
//                }
//            }
//        }
//        else if (requestCode == SETTING_CAMERA_PERMISSION) {
//            val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            val permissionForCamera = android.Manifest.permission.CAMERA
//            val res = checkCallingOrSelfPermission(permission)
//            val res_cam = checkCallingOrSelfPermission(permissionForCamera)
//            if (res == PackageManager.PERMISSION_GRANTED && res_cam == PackageManager.PERMISSION_GRANTED) {
//                captureImageIntent()
//            } else {
//                requestPermissionFromSettingsForCamera()
//            }
//        }
//        else if(requestCode == SETTING_GALLERY_PERMISSION){
//            val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            val res = checkCallingOrSelfPermission(permission)
//            if (res == PackageManager.PERMISSION_GRANTED) {
//                selectPictureFromGallery()
//            } else {
//                requestPermissionFromSettingsForGallery()
//            }
//        }
//        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            var result = CropImage.getActivityResult(data);
//            if (resultCode == Activity.RESULT_OK) {
//                var resultUri = result.getUri();
//                if(GreenSpoonPicker.mImagePickedListener!=null){
//                    GreenSpoonPicker.mImagePickedListener!!.onImagePickSuccess(resultUri)
//                    finish()
//                }
//
//            }
//            else if(resultCode == Activity.RESULT_CANCELED){
//                if(GreenSpoonPicker.mImagePickedListener!=null){
//                    GreenSpoonPicker.mImagePickedListener!!.onImagePickFailed()
//                    finish()
//                }
//            }
//            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                if(GreenSpoonPicker.mImagePickedListener!=null){
//                    GreenSpoonPicker.mImagePickedListener!!.onImagePickFailed()
//                    finish()
//                }
//            }
//        }
//        else if(requestCode == REQUEST_GALLERY_IMAGE){
//            if(resultCode == Activity.RESULT_OK) {
//                try {
//                    if (data != null) {
//                        var mGalleryImage = data.getData()
//                        if (mGalleryImage != null) {
//                            if (isCropEnable) {
//                                CropImage.activity(mGalleryImage).start(this@ImageCaptureActivity);
//                            } else if (GreenSpoonPicker.mImagePickedListener != null) {
//                                GreenSpoonPicker.mImagePickedListener!!.onImagePickSuccess(mGalleryImage)
//                                finish()
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//
//                }
//            }
//            else{
//                if(GreenSpoonPicker.mImagePickedListener!=null){
//                    GreenSpoonPicker.mImagePickedListener!!.onImagePickFailed()
//                    finish()
//                }
//            }
//        }
//        else {
//            finish()
//        }
//    }
//
//    fun dialog_with_callbackCancelable(mActivity: Activity, message: String, mOnClickListener: View.OnClickListener) {
//        var builder1 = AlertDialog.Builder(mActivity);
//        builder1.setTitle("App Title");
//        builder1.setMessage(message);
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton("OK",
//                object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        mOnClickListener.onClick(null)
//                        dialog!!.dismiss()
//                    }
//                })
//        if(!isForcefullyGetPermission) {
//            builder1.setNegativeButton("Cancel",
//                    object : DialogInterface.OnClickListener {
//                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            dialog!!.dismiss()
//                            finish()
//                        }
//                    })
//        }
//
//        var alert11 = builder1.create();
//        alert11.show();
//    }
//
//    private fun openImagePickerDialog() {
//        val builder = android.support.v7.app.AlertDialog.Builder(this)
//        val layout = View.inflate(this, R.layout.dialog, null)
//        builder.setView(layout)
//        alertDialog = builder.create()
//        alertDialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//
//        val txt_take_photo = layout.findViewById(R.id.txt_take_photo) as TextView
//        val txt_choose_from_gallery = layout.findViewById(R.id.txt_choose_from_gallery) as TextView
//        val txt_cancel = layout.findViewById(R.id.txt_cancel) as TextView
//
//        txt_take_photo.setOnClickListener {
//            captureImageForCamera()
//            alertDialog!!.dismiss()
//        }
//
//        txt_choose_from_gallery.setOnClickListener {
//            pickGalleryImage()
//            alertDialog!!.dismiss()
//        }
//
//        txt_cancel.setOnClickListener {
//            // Close dialog
//            alertDialog!!.dismiss()
//        }
//        alertDialog!!.show()
//    }
//
//}
