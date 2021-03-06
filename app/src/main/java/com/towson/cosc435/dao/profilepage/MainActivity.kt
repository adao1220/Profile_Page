package com.towson.cosc435.dao.profilepage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.towson.cosc435.dao.profilepage.Interface.ISettingRepository
import com.towson.cosc435.dao.profilepage.model.SettingModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    private lateinit var settingVar: ISettingRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingVar = DefaultRepository()
        displayProfile()


        //Set default image in drawable folder
        pp_profile_image.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.me))

        name_layout.setOnClickListener{edit("Name")}
        phone_layout.setOnClickListener{edit("Phone")}
        email_layout.setOnClickListener{edit("Email")}
        bio_layout.setOnClickListener{edit("Bio")}

        // for some reason, needed to add these ClickListener to make it work "better"
        pp_bio_info.setOnClickListener{edit("Bio")}
        pp_profile_name_info.setOnClickListener{edit("Name")}


        pp_profile_image_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to change your profile picture?" )

            builder.setTitle("Take a look at yourself!")
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { dialog, which -> newProfilePicture() }
            builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
            val alertDialog = builder.create()
            alertDialog.show();
        }
    }

    private fun newProfilePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else
                imageGallery()
        } else
            imageGallery()
    }

    private fun displayProfile() {
        when(val toSet = settingVar.getSetting(0)){
            null ->{
                Toast.makeText(this, "Something went wrong here", Toast.LENGTH_SHORT).show()
            } else->{
            val profileName = toSet.profileName
            val phoneNumber = toSet.phoneNumber
            val email = toSet.email
            val bio = toSet.bio

            pp_profile_name_info.setText(profileName).toString()
            pp_phone_number_info.setText(phoneNumber).toString()
            pp_email_info.setText(email).toString()
            pp_bio_info.setText(bio).toString()
        }
        }
    }

    private fun edit(key: String){
        val getSet = settingVar.getSetting(0)
        val json = Gson().toJson(getSet)
        val intent = Intent(this, EditPage::class.java)
        UPDATE_KEY = key
        intent.putExtra(UPDATE_KEY, json)
        startActivityForResult(intent,UPDATE_REQUEST_CODE)
    }

    private fun imageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    imageGallery()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            pp_profile_image.setImageURI(data?.data)
        }
            when(requestCode){
                UPDATE_REQUEST_CODE->{
                    when(resultCode){
                        Activity.RESULT_OK ->{
                            val json = data?.getStringExtra(EditPage.UPDATE_EXTRA_KEY)
                            when(json){
                                null->return
                                else->{
                                    val newSetting = Gson().fromJson(json, SettingModel::class.java)
                                    settingVar.editSettings(0,newSetting)
                                    displayProfile()
                                }
                            }
                        }
                    }
                }
            }
    }
    companion object{
        val TAG = "TEST"
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        val UPDATE_REQUEST_CODE = 1
        var UPDATE_KEY = ""
    }


}