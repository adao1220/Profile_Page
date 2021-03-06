package com.towson.cosc435.dao.profilepage

import com.towson.cosc435.dao.profilepage.Interface.ISettingRepository
import com.towson.cosc435.dao.profilepage.model.SettingModel

class DefaultRepository: ISettingRepository {
    val set: MutableList<SettingModel> = mutableListOf()

    init{
        set.add(SettingModel("Alexander Dao","(202)758-8602","alexander.p.dao@gmail.com", "I love food and sleeping"))
    }

    override fun getSettings(): List<SettingModel> {
        return set
    }

    override fun getSetting(idx: Int): SettingModel {
        return set[0]
    }

    override fun editSettings(idx: Int, setting: SettingModel){
        set[0] = setting
    }

}