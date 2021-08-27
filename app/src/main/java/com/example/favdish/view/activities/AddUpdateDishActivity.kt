package com.example.favdish.view.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Gallery
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.databinding.DialogboxBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.view.adapters.CustomListItemAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity() ,View.OnClickListener{

    private lateinit var addUpdateDishBinding: ActivityAddUpdateDishBinding
    private  var imagePath:String=""
    lateinit var myDialog:Dialog

    private val mFavDishViewModel:FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addUpdateDishBinding= ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(addUpdateDishBinding.root)
        setUpActionBar()



        addUpdateDishBinding.cameraImage.setOnClickListener(this)
        addUpdateDishBinding.type.setOnClickListener(this)
        addUpdateDishBinding.category.setOnClickListener(this)
        addUpdateDishBinding.cookingTime.setOnClickListener(this)
        addUpdateDishBinding.button.setOnClickListener(this)
    }

    private fun setUpActionBar(){
        setSupportActionBar(addUpdateDishBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addUpdateDishBinding.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if(v!=null){
            when(v.id){
                R.id.camera_image->{
                    showMyDialogBox()
                    return
                }
                R.id.type->{
                    CustomItemListDialog(resources.getString(R.string.dish_type),Constants.dishTypes(),Constants.DISH_TYPE)
                    return
                }
                R.id.category->{
                    CustomItemListDialog(resources.getString(R.string.dish_category),Constants.dishCategory(),Constants.DISH_CATEGORY)
                   return
                }
                R.id.cooking_time->{
                    CustomItemListDialog(resources.getString(R.string.cooking_time),Constants.dishCookingTime(),Constants.DISH_COOKING_TIME)
                   return
                }
                R.id.button->{
                    ButtonPressed()
                    return
                }

            }
        }
    }

    private fun showMyDialogBox(){
        val dialog=Dialog(this)
        val dialogBinding:DialogboxBinding= DialogboxBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.show()

        dialogBinding.camera.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                dialog.dismiss()
            Dexter.withContext(this@AddUpdateDishActivity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
                .withListener(object :MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
//                                Toast.makeText(this@AddUpdateDishActivity,"USE CAMERA",Toast.LENGTH_SHORT).show()
                           try {
                               startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),CAMERA)

                           }catch (e: Exception){
                               e.printStackTrace()
                           }
                            }
                            else{
                                setPermissionRationale()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {

                        setPermissionRationale()
                        token?.continuePermissionRequest()
                    }

                }).onSameThread().check()

            }

        })

        dialogBinding.gallery.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                dialog.dismiss()
                Dexter.withContext(this@AddUpdateDishActivity)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.CAMERA
                    )
                    .withListener(object :MultiplePermissionsListener{
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            report?.let {
                                if(report.areAllPermissionsGranted()){
                                    try {
                                        startActivityForResult(
                                            Intent(
                                                Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            ), GALLERY
                                        )
                                    }catch (e:Exception){
                                        e.printStackTrace()
                                    }
                                }
                                else{
                                    setPermissionRationale()
                                }

                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                        ) {

                            setPermissionRationale()
                            token?.continuePermissionRequest()
                        }

                    }).onSameThread().check()

            }

        })
    }

    private fun setPermissionRationale(){
        AlertDialog.Builder(this).setMessage("It looks like you have denied the permission, which" +
                "is needed for this feature please allow in setting to use further.")
            .setPositiveButton("GO TO SETTINGS"){
                dialog,second,->
                try {
                    val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri=Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                dialog,second,->
                dialog.dismiss()
            }.show()
    }

    companion object{
        private const val CAMERA=1
        private const val GALLERY=2
        private const val  IMAGE_DIRECTORY="FavDihImages"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            if(requestCode==CAMERA) {
             data?.extras?.let {
                 val imageBitmap=data?.extras?.get("data") as Bitmap
                 Glide.with(this@AddUpdateDishActivity)
                     .load(imageBitmap)
                     .centerCrop()
                     .into(addUpdateDishBinding.mainImage)
//                 addUpdateDishBinding.mainImage.setImageBitmap(imageBitmap)

                imagePath= saveImageToInternalStorage(imageBitmap)
                 Log.e("imagePath",imagePath)

                 addUpdateDishBinding.cameraImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateDishActivity,R.drawable.ic_edit))
                }

            }else if(requestCode==GALLERY){
                data?.let {
                    val PhotoUri=data?.data
                    Glide.with(this@AddUpdateDishActivity)
                        .load(PhotoUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .listener(object:RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("failed","failed",e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val imageBitmap:Bitmap=resource.toBitmap()
                                    imagePath=saveImageToInternalStorage(imageBitmap)
                                    Log.e("imagePath",imagePath)
                                }
                               return false
                            }

                        })
                        .into(addUpdateDishBinding.mainImage)

//                    addUpdateDishBinding.mainImage.setImageURI(PhotoUri)
                }
            }
            }else if(resultCode== RESULT_CANCELED){
                Log.e("error","cancelled")
            }
        }


    private fun saveImageToInternalStorage(bitmap:Bitmap):String{
        val wrapper=ContextWrapper(applicationContext)
        var file=wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file= File(file,"${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch(e : IOException){
          e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun CustomItemListDialog(title:String,list:List<String>,selection:String){
         myDialog=Dialog(this)
        val binding:DialogCustomListBinding= DialogCustomListBinding.inflate(layoutInflater)
        myDialog.setContentView(binding.root)
        binding.tvSelect.text=title
        binding.rvItem.layoutManager=LinearLayoutManager(this)

        val adapter=CustomListItemAdapter(this,list,selection)
        binding.rvItem.adapter=adapter
        myDialog.show()
    }

    fun selectedItem(item:String,selection: String){
        when(selection){
            Constants.DISH_TYPE->{
                addUpdateDishBinding.type.setText(item)
                myDialog.dismiss()
                return
            }
            Constants.DISH_CATEGORY->{
                addUpdateDishBinding.category.setText(item)
                myDialog.dismiss()
                return
            }
            Constants.DISH_COOKING_TIME->{
                addUpdateDishBinding.cookingTime.setText(item)
                myDialog.dismiss()
                return
            }
        }
    }

    fun ButtonPressed(){
        val imagePath=imagePath.toString().trim{it <= ' '}
        val title=addUpdateDishBinding.title.text.toString().trim{it <= ' '}
        val type=addUpdateDishBinding.type.text.toString().trim{it <= ' '}
        val category=addUpdateDishBinding.category.text.toString().trim{it <= ' '}
        val ingredients=addUpdateDishBinding.ingredients.text.toString().trim{it <= ' '}
        val cookingTime=addUpdateDishBinding.cookingTime.text.toString().trim{it <= ' '}
        val directionCook=addUpdateDishBinding.directionCook.text.toString().trim{it <= ' '}


        if(TextUtils.isEmpty((imagePath))){
            Toast.makeText(this@AddUpdateDishActivity,"No image selected",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(title)){
            Toast.makeText(this@AddUpdateDishActivity,"please enter the title",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(type)){
            Toast.makeText(this@AddUpdateDishActivity,"please select the type",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(category)){
            Toast.makeText(this@AddUpdateDishActivity,"please select the category",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(ingredients)){
            Toast.makeText(this@AddUpdateDishActivity,"please enter the ingredients",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(cookingTime)){
            Toast.makeText(this@AddUpdateDishActivity,"please select the cooking time",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(directionCook)){
            Toast.makeText(this@AddUpdateDishActivity,"please enter the direction",Toast.LENGTH_SHORT).show()
        } else{

            val favDishDetails:FavDish=FavDish(
                imagePath,
                Constants.DISH_IMAGE_SOURCE_LOCAL,
                title,
                type,
                category,
                ingredients,
                cookingTime,
                directionCook,
                false
            )
            mFavDishViewModel.insert(favDishDetails)
            Toast.makeText(this@AddUpdateDishActivity,"data success upload!!",Toast.LENGTH_SHORT).show()
            finish()
        }


    }


}