package com.towson.cosc435.dao.profilepage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.towson.cosc435.dao.profilepage.Interface.ISettingRepository
import com.towson.cosc435.dao.profilepage.model.SettingModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var settingVar: ISettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingVar = DefaultRepository()

        displayProfile()

        name_layout.setOnClickListener{Edit("Name")}
        phone_layout.setOnClickListener{Edit("Phone")}
        email_layout.setOnClickListener{Edit("Email")}
        bio_layout.setOnClickListener{Edit("Bio")}
        // for some reason, needed to added this to make the large box clickable
        pp_bio_info.setOnClickListener{Edit("Bio")}

    }

    private fun displayProfile() {
        when(val toSet = settingVar.getSetting(0)){
            null ->{
                test()
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

    fun test() {
        Toast.makeText(this@MainActivity, "HEH...there was a problem", Toast.LENGTH_SHORT).show()
    }

    fun Edit(key: String){
        val getSet = settingVar.getSetting(0)
        val json = Gson().toJson(getSet)
        val intent = Intent(this, EditPage::class.java)
        UPDATE_KEY = key
        intent.putExtra(UPDATE_KEY, json)
        startActivityForResult(intent,EMAIL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            EMAIL_REQUEST_CODE->{
                when(resultCode){
                    Activity.RESULT_OK ->{
                        val json = data?.getStringExtra(EditPage.EMAIL_EXTRA_KEY)
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
        val EMAIL_REQUEST_CODE = 1
        var UPDATE_KEY = ""
        var load = 0
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}