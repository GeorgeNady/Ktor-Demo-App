package com.george.ktorapp.ui.activities.main.fragments

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.george.ktorapp.R
import com.george.ktorapp.base.ActivityFragmentAnnoation
import com.george.ktorapp.base.BaseFragment
import com.george.ktorapp.databinding.FragmentMyProfile2Binding
import com.george.ktorapp.utiles.GlideImageLoader
import com.george.ktorapp.utiles.ImageChooserUtility
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import com.george.ktorapp.viewmodel.fragmentsViewModels.MainFragmentViewModel
import com.george.ktorapp.viewmodel.fragmentsViewModels.MyPostsFragmentViewModel
import com.george.ktorapp.utiles.Routes.MY_PROFILE_ROUTE2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.makeramen.roundedimageview.RoundedImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@ActivityFragmentAnnoation(MY_PROFILE_ROUTE2)
class MyProfile2Fragment : BaseFragment<FragmentMyProfile2Binding>() {

    companion object {
        const val REQUEST_GALLERY_PHOTO = 11
        const val REQUEST_CAMERA_PHOTO = 12
    }
    private var file: File? = null
    private var bottomSheetDialog: BottomSheetDialog? = null


    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: MyPostsFragmentViewModel
    lateinit var mainViewModel : MainFragmentViewModel


    override fun initialization() {
        viewModel = ViewModelProvider(this).get(MyPostsFragmentViewModel::class.java)
        mainViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)

        // setupRecyclerView()
    }

    override fun setListener() {

        binding?.apply {
            btnEditAvatar.setOnClickListener {
                val sheetView: View =
                    layoutInflater.inflate(R.layout.change_photo_bottom_sheet_layout, null)
                val removePhoto = sheetView.findViewById<LinearLayout>(R.id.removePhoto)
                val cancel = sheetView.findViewById<LinearLayout>(R.id.cancel)
                val roundImageTop = sheetView.findViewById<RoundedImageView>(R.id.roundImageTop)
                val takePhoto = sheetView.findViewById<LinearLayout>(R.id.takePhoto)
                val chooseGallery = sheetView.findViewById<LinearLayout>(R.id.choose_gallery)

                GlideImageLoader(
                    roundImageTop,
                    null
                ).load(prefs.prefsUserAvatar, options)
                cancel
                    .setOnClickListener {
                        bottomSheetDialog!!.dismiss()
                    }
                removePhoto.setOnClickListener {
                    /*viewModelProfile.deleteAvatar(requireContext()).observe(this@MyProfile2Fragment,
                        { user ->
                            Toast.makeText(requireContext(),"Photo removed",Toast.LENGTH_SHORT).show()
                            GlideImageLoader(roundImageTop,
                                null
                            ).load(user.avatar, options)
                            GlideImageLoader(
                                binding!!.ivUserAvatar,
                                null
                            ).load(user.avatar, options)
                            prefs.prefsUserAvatar = user.avatar
                            bottomSheetDialog!!.dismiss()
                        })*/
                    bottomSheetDialog!!.dismiss()
                }
                takePhoto.setOnClickListener {
                    requestStoragePermission(requireActivity(), true)
                    bottomSheetDialog!!.dismiss()
                }
                chooseGallery
                    .setOnClickListener {
                        requestStoragePermission(requireActivity(), false)
                        bottomSheetDialog!!.dismiss()
                    }
                bottomSheetDialog!!.setContentView(sheetView)
                bottomSheetDialog!!.show()
            }
        }

    }

    private fun requestStoragePermission(context: Activity, isCamera: Boolean) {
        Dexter.withActivity(context)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            dispatchTakePictureIntent()
                        } else {
                            dispatchGalleryIntent()
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog(context)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error: DexterError? ->
                Toast.makeText(context, "Error happened! ", Toast.LENGTH_SHORT)
                    .show()
            }
            .onSameThread()
            .check()
    }

    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(
            pickPhoto,
            REQUEST_GALLERY_PHOTO
        )
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            intent,
            REQUEST_CAMERA_PHOTO
        )
    }

    fun showSettingsDialog(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage(
            "This app needs permission to use this feature. You can give them in the app settings."
        )
        builder.setPositiveButton("Go to settings") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings(context)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        // Create the alert dialog and change Buttons colour
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.resources.getColor(R.color.green))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.resources.getColor(R.color.red))
            //dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black));
        }
        dialog.show()
    }

    fun openSettings(context: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK && null != data) {

            // Get the Image from data
            val selectedImage = data.data

            val bitmap: Bitmap
            try {
                bitmap = ImageChooserUtility.getImageFromResult(requireContext(), resultCode, data)
                file = File(getRealPathFromUri(selectedImage))
                binding!!.ivUserAvatar.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_CAMERA_PHOTO && resultCode == Activity.RESULT_OK && null != data) {
            val thumbnail = data.extras!!["data"] as Bitmap?
            val bytes = ByteArrayOutputStream()
            thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            file = File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg"
            )
            val fo: FileOutputStream
            try {
                file!!.createNewFile()
                fo = FileOutputStream(file)
                fo.write(bytes.toByteArray())
                fo.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            binding!!.ivUserAvatar.setImageBitmap(thumbnail)
        }
    }

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireContext().contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }

}