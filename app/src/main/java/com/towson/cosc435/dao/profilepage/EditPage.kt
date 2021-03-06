package com.towson.cosc435.dao.profilepage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.towson.cosc435.dao.profilepage.MainActivity.Companion.UPDATE_KEY
import com.towson.cosc435.dao.profilepage.model.SettingModel
import kotlinx.android.synthetic.main.activity_edit_page.*


class EditPage : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        //reset the errorCheck
        ErrorCheck = 0

        val findKey = intent.getStringExtra(UPDATE_KEY)
        val toSet = Gson().fromJson(findKey, SettingModel::class.java)

        if (toSet != null) {
            profileName = toSet.profileName
            phone = toSet.phoneNumber
            email = toSet.email
            bio = toSet.bio
            when (UPDATE_KEY) {
                //steps for the following:
                //1) Make correct boxes visible
                //2) Set header for given section
                //2) load the saved variable--> if no variable, load nothing
                "Name" -> {
                    //todo: try a linear layout later
                    //turn a string into two string. first AND last
                    ep_name_layout.visibility = View.VISIBLE
                    ep_header.setText(getString(R.string.update_name_header)).toString()
                    val first = profileName.split(" ")[0]
                    val last = profileName.split(" ")[1]
                    ep_first_name_edit.setText(first).toString()
                    ep_last_name_edit.setText(last).toString()
                }
                "Phone" -> {
                    //todo: make sure phonenumber is set like this -> (xxx) xxx- xxxx
                    ep_phone_layout.visibility = View.VISIBLE
                    ep_header.setText(getString(R.string.update_phone_header)).toString()
                    ep_phone_edit.setText(phone).toString()
                }
                "Email" -> {
                    ep_email_layout.visibility = View.VISIBLE
                    ep_header.setText(getString(R.string.update_email_header)).toString()
                    ep_email_edit.setText(email).toString()
                }
                "Bio" -> {
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
        when (view?.id) {
            R.id.ep_update_btn -> {
                val intent = Intent()
                when (UPDATE_KEY) {
                    "Name" -> {
                        first = ep_first_name_edit.editableText.toString()
                        last = ep_last_name_edit.editableText.toString()

                        // Check if both first and last name entry are filled or not
                        if (isNullOrEmpty(first) || isNullOrEmpty(last)) {
                            ErrorCheck = 1
                            ErrorMessage = "Please enter your full name"
                        } else {
                            ErrorCheck = 0
                            //will get rid of the spaces in the first/last name
                            first = first.replace("\\s".toRegex(), "")
                            last = last.replace("\\s".toRegex(), "")
                            //will capitalize the first letter in their first/last name
                            profileName = first.capitalize() + " " + last.capitalize()
                        }
                    }
                    "Phone" -> {
                        phone = ep_phone_edit.editableText.toString()
                        //Checking if the Phone Number is exactly 10 numbers
                        val phoneNumCheck = phone.filter { it.isDigit() }
                        val length = phoneNumCheck.length
                        if (length == 10) {
                            ErrorCheck = 0
                            phone = "(" + phoneNumCheck.substring(0,3) + ")" + " " + phoneNumCheck.substring(3,6) + "-" + phoneNumCheck.substring(6, 10)
                        } else {
                            ErrorCheck = 1
                            ErrorMessage = "Invalid Phone number"
                        }
                    }
                    "Email" -> {
                        email = ep_email_edit.editableText.toString()
                        email = email.replace("\\s".toRegex(), "")
                        //checking two things:
                        //1) If it has a "@" in the email
                        //2) If the last 4 characters are ".com" or ".edu"
                        if (isNullOrEmpty(email)) {
                            ErrorCheck = 1
                            ErrorMessage = "Please enter your email"
                        } else {
                            if (email.contains("@")) {
                                val emailCheck = email.takeLast(4)
                                if (emailCheck.contains(".com") || emailCheck.contains(".edu")) {
                                    ErrorCheck = 0
                                } else {
                                    ErrorCheck = 1
                                    ErrorMessage = "Needs to have '.com' or 'edu' at the end"
                                }
                            } else {
                                ErrorCheck = 1
                                ErrorMessage = "You are missing '@' in your email!"
                            }
                        }
                    }
                    "Bio" -> {
                        bio = ep_bio_edit.editableText.toString()
                        val count = bioWordCheck(bio)
                        if(count >= 3){
                            ErrorCheck=0
                        }
                        else if(count<3 && count >0 ){
                            ErrorCheck=1
                            ErrorMessage = "Say more than " + count + " word(s)!"
                        }
                        else{
                            ErrorCheck = 1
                            ErrorMessage = "You left this blank! Tell us about yourself!!!"
                        }
                    }
                }
                if (ErrorCheck == 1) {
                    Toast.makeText(this, ErrorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val updatedProfile = SettingModel(profileName, phone, email, bio)
                        val json = Gson().toJson(updatedProfile)
                        intent.putExtra(UPDATE_EXTRA_KEY, json)
                        setResult(Activity.RESULT_OK, intent)
                        goneEverything()
                    } catch (ex: Exception) {
                        Toast.makeText(this, "Invalid somehow", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.ep_back_btn -> {
                goneEverything()
            }
        }
    }

    private fun bioWordCheck(bio: String):Int{
        Log.i(TAG, "Number of words: "+ bio)

        val strArray = bio.split(" ".toRegex()).toTypedArray()
        var count = 0
        for (s in strArray) {
            if (s != "") {
                count++
            }
        }
        Log.i(TAG, "Number of words: "+ count)
        return count
    }


    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

    fun goneEverything() {
        ep_name_layout.visibility = View.GONE
        ep_phone_layout.visibility = View.GONE
        ep_email_layout.visibility = View.GONE
        ep_bio_edit.visibility = View.GONE
        finish()
    }

    companion object {
        val UPDATE_EXTRA_KEY = "UPDATE"
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