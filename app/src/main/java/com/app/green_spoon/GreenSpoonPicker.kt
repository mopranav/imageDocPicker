package com.app.green_spoon

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File

public class GreenSpoonPicker(var mActivity:AppCompatActivity,
                              var isForcefullyGetPermission : Boolean,
                              var code : Int,
                              var mImagePickedListener : ImagePickedListener,
                              var grantPathName: String) : BaseClass(), OnPermissionListener {

    var fileNameFromUser = ""
    var WRITE_STORAGE_STRING_PERMISSION = ""
    var CAMERA_STRING_PERMISSION  = ""

    fun setStoragePermission(WRITE_STORAGE_STRING_PERMISSION: String){
        this.WRITE_STORAGE_STRING_PERMISSION=WRITE_STORAGE_STRING_PERMISSION
    }


    fun setFileName(fileNameFromUser : String){
        this.fileNameFromUser = fileNameFromUser
    }

    fun setCameraPermission(CAMERA_STRING_PERMISSION: String){
        this.CAMERA_STRING_PERMISSION=CAMERA_STRING_PERMISSION
    }

    var CAPTURE_MESSAGE = "Camera and Storage Permissions are Required. Please Press Ok Button to Go to Permission Settings and Enable Permissions."
    var GALLERY_MESSAGE = "Storage Permissions is Required. Please Press Ok Button to Go to Permission Settings and Enable Permissions."
    var NEVER_ASK_GALLERY="No Gallery Permission";
    var NEVER_ASK_CAMERA="no cam or storage";

    var APP_TITLE="";

    val CAMERA_PERMISSION = 2512
    val STORAGE_PERMISSION = 6325

    val SETTING_CAMERA_PERMISSION = 5454
    val SETTING_GALLERY_PERMISSION = 5954

    val REQUEST_IMAGE_CAPTURE = 6542
    val REQUEST_GALLERY_IMAGE = 1234
    val ONLY_STORAGE_PERMISSION = 4546

    var alertDialog : AlertDialog? = null

    //Conditions
//    var isForcefullyGetPermission = false

//    var isCropEnable: Boolean = false
//    var code: Int = 2

//    var CAPTURE_MESSAGE = ""
//    var GALLERY_MESSAGE = ""

    private var mFileImage: File? = null
    private var mImageCaptureUri: Uri? = null

    companion object {
//        var mImagePickedListener : ImagePickedListener? = null

        public const val CODE_PICK_IMAGE = 0
        public const val CODE_GALLERYIMAGE = 1
        public const val CODE_DIALOG = 2
    }

    fun setMessageForCaptureDialog(message : String){
        CAPTURE_MESSAGE = message
    }

    fun setMessageForGalleryDialog(message : String){
        GALLERY_MESSAGE = message
    }

    fun setMessageForNeveraskCameraDialog(message : String){
        NEVER_ASK_CAMERA = message
    }

    fun setMessageForNeveraskGalleryDialog(message : String){
        NEVER_ASK_GALLERY = message
    }
    fun setApptitle(message : String){
        APP_TITLE = message
    }


    fun start(){
        if(code!=0 && code!=1 && code!=2){
            this.code=2
        }
        else{
            this.code = code
        }

        if(code == GreenSpoonPicker.CODE_DIALOG){
            openImagePickerDialog()
        }
        else if(code == GreenSpoonPicker.CODE_GALLERYIMAGE){
            pickGalleryImage()
        }
        else if(code == GreenSpoonPicker.CODE_PICK_IMAGE){
            captureImageForCamera()
        }

//        var mIntent = Intent(mActivity,ImageCaptureActivity::class.java)
//        mIntent.putExtra("isForce", isForcefullyGetPermission)
//        mIntent.putExtra("crop", isCropEnable)
//        mIntent.putExtra("code", code)
//        mIntent.putExtra("cap_msg", CAPTURE_MESSAGE)
//        mIntent.putExtra("gal_msg", GALLERY_MESSAGE)
//        mActivity.startActivity(mIntent)
    }

    public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,grantResults: IntArray) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED) {
                if (mOnPermissionListener != null) mOnPermissionListener.onPermissionGranted(requestCode)
                break
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                if (mOnPermissionListener != null) mOnPermissionListener.onPermissionDenied(requestCode)
                break
            } else {
                if (mOnPermissionListener != null)
                    mOnPermissionListener.onPermissionNeverAsk(requestCode)
                break
            }
        }
    }

    fun captureImageForCamera(){
        checkForPermission(CAMERA_STRING_PERMISSION,CAMERA_PERMISSION,this@GreenSpoonPicker)
    }

    fun pickGalleryImage(){
        checkForPermission(WRITE_STORAGE_STRING_PERMISSION,ONLY_STORAGE_PERMISSION,this@GreenSpoonPicker)
    }

    fun checkForPermission(requestedPermissions: String, code: Int, mOnPermissionListener: OnPermissionListener) {
        requestAppPermissions(arrayOf(requestedPermissions), code,object : OnPermissionListener {
            override fun onPermissionGranted(requestCode: Int) {
                Log.e("onPermissionGranted", "onPermissionGranted")
                mOnPermissionListener.onPermissionGranted(requestCode)
            }

            override fun onPermissionDenied(requestCode: Int) {
                Log.e("onPermissionDenied", "onPermissionDenied")
                mOnPermissionListener.onPermissionDenied(requestCode)
            }

            override fun onPermissionNeverAsk(requestCode: Int) {
                mOnPermissionListener.onPermissionNeverAsk(requestCode)
            }
        },mActivity)
    }


    override fun onPermissionGranted(requestCode: Int) {
        if (requestCode == CAMERA_PERMISSION) {
            checkForPermission(
                    WRITE_STORAGE_STRING_PERMISSION,
                    STORAGE_PERMISSION,
                    this@GreenSpoonPicker
            )
        } else if (requestCode == STORAGE_PERMISSION) {
            captureImageIntent()
        }
        else if(requestCode==ONLY_STORAGE_PERMISSION){
            selectPictureFromGallery()
        }
    }
    override fun onPermissionDenied(requestCode: Int) {
        if (requestCode == CAMERA_PERMISSION) {
            requestPermissionFromSettingsForCamera()
        }
        else if(requestCode == STORAGE_PERMISSION){
            requestPermissionFromSettingsForCamera()
        }
        else if(requestCode==ONLY_STORAGE_PERMISSION){
            requestPermissionFromSettingsForGallery()
        }
    }
    override fun onPermissionNeverAsk(requestCode: Int) {
        if(requestCode== ONLY_STORAGE_PERMISSION){
            dialog_with_callbackCancelable(mActivity,
                    NEVER_ASK_GALLERY,
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            mActivity.startActivityForResult(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", mActivity.packageName, null)
                            }, SETTING_GALLERY_PERMISSION)
                        }
                    })
        }
        else {
            dialog_with_callbackCancelable(mActivity,
                    NEVER_ASK_CAMERA,
                    object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            mActivity.startActivityForResult(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", mActivity.packageName, null)
                            }, SETTING_CAMERA_PERMISSION)
                        }
                    })
        }
    }


    fun captureImageIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mFileImage = createImageFile()


        mImageCaptureUri = FileProvider.getUriForFile(mActivity,mActivity.applicationContext.packageName+".provider",mFileImage!!)
        var resInfoList = mActivity.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            mActivity.grantUriPermission(packageName, mImageCaptureUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
        mActivity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    fun createImageFile(): File {
        var image: File
        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES)

        var storageDir = File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+grantPathName)
        storageDir.mkdirs()

        if(fileNameFromUser.trim().equals("")) {
            image = File.createTempFile(
                    "temp", /* prefix */
                    ".jpg", /* suffix */
                    storageDir      /* directory */
            )
        }
        else{
            image = File.createTempFile(
                    fileNameFromUser, /* prefix */
                    ".jpg", /* suffix */
                    storageDir      /* directory */
            )
        }

        return image
    }

    fun requestPermissionFromSettingsForCamera() {
        dialog_with_callbackCancelable(mActivity,
                CAPTURE_MESSAGE,
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        mActivity.startActivityForResult(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", mActivity.packageName, null)
                        }, SETTING_CAMERA_PERMISSION)
                    }
                })
    }


    fun requestPermissionFromSettingsForGallery() {
        dialog_with_callbackCancelable(mActivity,
                GALLERY_MESSAGE,
                object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        mActivity.startActivityForResult(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", mActivity.packageName, null)
                        }, SETTING_GALLERY_PERMISSION)
                    }
                })
    }

    private fun selectPictureFromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        mActivity.startActivityForResult(photoPickerIntent, REQUEST_GALLERY_IMAGE)
    }

    public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if(resultCode == Activity.RESULT_OK) {
                if (mImagePickedListener != null) {
                    var mUri = FileProvider.getUriForFile(mActivity,mActivity.packageName+".provider",mFileImage!!)
                    mImagePickedListener!!.onCaptureSuccess(mUri)
                    mImagePickedListener!!.onImagePickSuccess(mUri,mFileImage!!.absolutePath)
                }
            }
            else{
                if (mImagePickedListener != null) {
                    mImagePickedListener!!.onImagePickFailed()

                }
            }
        }
        else if (requestCode == SETTING_CAMERA_PERMISSION) {
            val permission = WRITE_STORAGE_STRING_PERMISSION
            val permissionForCamera = CAMERA_STRING_PERMISSION
            val res = mActivity.checkCallingOrSelfPermission(permission)
            val res_cam = mActivity.checkCallingOrSelfPermission(permissionForCamera)
            if (res == PackageManager.PERMISSION_GRANTED && res_cam == PackageManager.PERMISSION_GRANTED) {
                captureImageIntent()
            } else {
                requestPermissionFromSettingsForCamera()
            }
        }
        else if(requestCode == SETTING_GALLERY_PERMISSION){
            val permission = WRITE_STORAGE_STRING_PERMISSION
            val res = mActivity.checkCallingOrSelfPermission(permission)
            if (res == PackageManager.PERMISSION_GRANTED) {
                selectPictureFromGallery()
            } else {
                requestPermissionFromSettingsForGallery()
            }
        }
        else if(requestCode == REQUEST_GALLERY_IMAGE){
            if(resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        var mGalleryImage = data.getData()
                        if (mGalleryImage != null) {
                            if (mImagePickedListener != null) {
                                var mPath : String? = ""
                                try {
                                    mPath = getRealPathFromURI(mActivity, mGalleryImage)
                                }
                                catch (ex:Exception){

                                }
                                mImagePickedListener!!.onImagePickSuccess(mGalleryImage,mPath)
                            }
                        }
                    }
                } catch (e: Exception) {

                }
            }
            else{
                if(mImagePickedListener!=null){
                    mImagePickedListener!!.onImagePickFailed()
                }
            }
        }
        else {

        }
    }

    fun dialog_with_callbackCancelable(mActivity: Activity, message: String, mOnClickListener: View.OnClickListener) {
        var builder1 = AlertDialog.Builder(mActivity);
        builder1.setTitle(APP_TITLE);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton("OK",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        mOnClickListener.onClick(null)
                        dialog!!.dismiss()
                    }
                })
        if(!isForcefullyGetPermission) {
            builder1.setNegativeButton("Cancel",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()

                        }
                    })
        }

        var alert11 = builder1.create();
        alert11.show();
    }

    private fun openImagePickerDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(mActivity)
        val layout = View.inflate(mActivity, R.layout.dialog, null)
        builder.setView(layout)
        alertDialog = builder.create()
        alertDialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val txt_take_photo = layout.findViewById(R.id.txt_take_photo) as TextView
        val txt_choose_from_gallery = layout.findViewById(R.id.txt_choose_from_gallery) as TextView
        val txt_cancel = layout.findViewById(R.id.txt_cancel) as TextView

        txt_take_photo.setOnClickListener {
            captureImageForCamera()
            alertDialog!!.dismiss()
        }

        txt_choose_from_gallery.setOnClickListener {
            pickGalleryImage()
            alertDialog!!.dismiss()
        }

        txt_cancel.setOnClickListener {
            // Close dialog
            alertDialog!!.dismiss()
        }
        alertDialog!!.show()
    }

    fun getRealPathFromURI(activity : Activity,contentURI: Uri): String {
        val result: String
        val cursor =activity.getContentResolver().query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor!!.moveToFirst()
            val idx = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor!!.getString(idx)
            cursor!!.close()
        }
        return result
    }

}