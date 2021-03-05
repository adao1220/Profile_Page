package com.towson.cosc435.dao.profilepage.Interface

import com.towson.cosc435.dao.profilepage.model.SettingModel

interface ISettingRepository {

    fun getSettings():List<SettingModel>
    fun getSetting(idx: Int):SettingModel
    fun editSettings(idx: Int, setting:SettingModel)
}