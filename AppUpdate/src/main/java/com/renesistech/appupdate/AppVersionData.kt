package com.renesistech.appupdate

import org.json.JSONObject
import java.io.Serializable

data class AppVersionData(
    var android_version: String="",
    var app_logo: String?=null,
    var app_name: String="",
    var app_slug: String="",
    var button_text: String="",
    var button_url: String="",
    var content: String="",
    var created_at: String="",
    var id: Int=-1,
    var ios_version: String ?=null,
    var updated_at: String=""
):Serializable {

    fun isAppRunnable(appVersion: String): Boolean {
        val serverDividedVersion = android_version.split(".")
        val serverVersionValue=(serverDividedVersion[0]+"."+serverDividedVersion[1]+serverDividedVersion[2]).toDouble()
        val localDividedVersion = appVersion.split(".")
        val localVersionValue=(localDividedVersion[0]+"."+localDividedVersion[1]+localDividedVersion[2]).toDouble()
        var isRunnable = false
        if (localVersionValue >= serverVersionValue) {
            isRunnable = true
        }
        return isRunnable
    }

    companion object{
        fun getVersionObject(jsonObject:JSONObject):AppVersionData{
            val appVersionData=AppVersionData()
            appVersionData.android_version=jsonObject.getString("android_version")
            appVersionData.app_name=jsonObject.getString("app_name")
            appVersionData.app_slug=jsonObject.getString("app_slug")
            appVersionData.button_text=jsonObject.getString("button_text")
            appVersionData.button_url=jsonObject.getString("button_url")
            appVersionData.content=jsonObject.getString("content")
            appVersionData.created_at=jsonObject.getString("created_at")
            appVersionData.id=jsonObject.getInt("id")
            appVersionData.ios_version=jsonObject.getString("ios_version")
            appVersionData.updated_at=jsonObject.getString("updated_at")
            appVersionData.app_logo=jsonObject.getString("app_logo")
            return appVersionData
        }
    }
}