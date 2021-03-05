package com.towson.cosc435.dao.profilepage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.towson.cosc435.dao.profilepage.MainActivity.Companion.UPDATE_KEY
import com.towson.cosc435.dao.profilepage.model.SettingModel
import kotlinx.android.synthetic.main.activity_edit_page.*

class EditPage : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)
        val findKey = intent.getStringExtra(UPDATE_KEY)

        val toSet = Gson().fromJson(findKey,SettingModel::class.java)
        if(toSet != null){
            profileName = toSet.profileName
            phone = toSet.phoneNumber
            email = toSet.email
            bio = toSet.bio

        when(UPDATE_KEY){
            //todo: Check padding on all of them

            //steps for the following:
            //1) Make correct boxes visible
            //2) Set header for given section
            //2) load the saved variable--> if no variable, load nothing
            "Name"->{
                //todo: try a linear layout later
                //turn a string into two string. first AND last
                ep_name_layout.visibility = View.VISIBLE
                ep_header.setText(getString(R.string.update_name_header)).toString()
                val first = profileName.split(" ")[0]
                val last = profileName.split(" ")[1]
                ep_first_name_edit.setText(first).toString()
                ep_last_name_edit.setText(last).toString()

            }
            "Phone"->{
                //todo: make sure phonenumber is set like this -> (xxx) xxx- xxxx
                ep_phone_layout.visibility = View.VISIBLE
                ep_header.setText(getString(R.string.update_phone_header)).toString()
                ep_phone_edit.setText(phone).toString()
            }
            "Email"->{
                ep_email_layout.visibility=View.VISIBLE
                ep_header.setText(getString(R.string.update_email_header)).toString()
                ep_email_edit.setText(email).toString()
            }
            "Bio"->{
                ep_bio_edit.visibility = View.VISIBLE
                ep_header.setText(getString(R.string.update_bio_header)).toString()
                ep_bio_edit.setText(bio).toString()
            }
        }

        }
        ep_update_btn.setOnClickListener(this)
        ep_back_btn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ep_update_btn->{
                val intent = Intent()
                when(UPDATE_KEY){
                    "Name"->{
                        first = ep_first_name_edit.editableText.toString()
                        last = ep_last_name_edit.editableText.toString()
                        // Check if both first and last name entry are filled or not
                        if(isNullOrEmpty(first) || isNullOrEmpty(last)) {
                            ErrorCheck =1
                            ErrorMessage = "Please fill out both boxes"
                        }else {
                            ErrorCheck =0
                            profileName = first + " " + last
                            ep_name_layout.visibility = View.GONE
                        }
                    }
                    "Phone"->{
                        phone = ep_phone_edit.editableText.toString()
                        val phoneNumCheck = phone.filter{it.isDigit()}
                        val length = phoneNumCheck.length
                        if(length == 10){
                            ErrorCheck =0
                            phone = "(" + phoneNumCheck.substring(0,3)+")" + " " + phoneNumCheck.substring(3,6) + "-" + phoneNumCheck.substring(6,10)
                            ep_phone_layout.visibility = View.GONE
                        }else{
                            ErrorCheck =1
                            ErrorMessage = "Invalid Phone number"
                        }
                    }
                    "Email"->{
                        email = ep_email_edit.editableText.toString()
                        if(isNullOrEmpty(email)){
                            ErrorCheck =1
                            ErrorMessage = "Please enter your email"
                        }else{
                            val emailCheck = email.takeLast(4)
                            if(emailCheck.contains(".com") || emailCheck.contains(".edu")){
                                ErrorCheck =0
                                ep_email_layout.visibility=View.GONE
                            }else{
                                ErrorCheck =1
                                ErrorMessage = "needs to have '.com' or 'edu' at the end"
                            }
                        }
                    }
                    "Bio"->{
                        bio = ep_bio_edit.editableText.toString()
                        if(isNullOrEmpty(bio)){
                            ErrorCheck =1
                            ErrorMessage = "You forgot to tell us about yourself!"

                            }else{
                            ep_bio_edit.visibility = View.GONE

                        }
                    }
                }
                if(ErrorCheck == 1){
                    Toast.makeText(this, ErrorMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    try{
                        val updatedProfile = SettingModel(profileName,phone, email, bio)
                        val json = Gson().toJson(updatedProfile)
                        //replace EMAIL_EXTRA_KEY with the right key (during the when block)
                        intent.putExtra(EMAIL_EXTRA_KEY,json)
                        setResult(Activity.RESULT_OK,intent)
                        goneEverything()
                        finish()
                    }catch(ex:Exception){
                        Toast.makeText(this, "Invalid somehow", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.ep_back_btn->{
                goneEverything()
                finish()
            }
        }
    }

    fun isNullOrEmpty(str: String?):Boolean{
        if(str!=null && !str.isEmpty())
            return false
        return true
    }

    fun goneEverything(){
        ep_name_layout.visibility = View.GONE
        ep_phone_layout.visibility = View.GONE
        ep_email_layout.visibility=View.GONE
        ep_bio_edit.visibility = View.GONE

        finish()
    }
    companion object{
        val EMAIL_EXTRA_KEY = "EMAIL"
        val TAG = "TEST"
        var profileName = ""
        var first = ""
        var last = ""
        var phone = ""
        var email = ""
        var bio = ""
        var ErrorCheck = 0
        var ErrorMessage = ""
    }
}