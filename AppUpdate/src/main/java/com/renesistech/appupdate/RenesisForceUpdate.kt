package com.renesistech.appupdate

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL
import java.net.URLConnection

class RenesisForceUpdate(activityRef: AppCompatActivity) {

    private var currentActivity: WeakReference<AppCompatActivity> = WeakReference(activityRef)
    private val GET_APP_VERSION = "https://force.renesistechdemo.com/get_app_data?slug="
    private var appVersionData: AppVersionData? = null
    private val appVersionDialogFragment by lazy { AppVersionDialogFragment() }
    private val progressDialog: AlertDialog? by lazy { getProgressDialog(currentActivity.get()) }
    private var messageCode = 0

    var buttonBackgroundColor:Int ? = null
    var buttonTextColor:Int ? = null
    var contentColor:Int ? = null
    var cardBackgroundColor:Int ? = null
    var defaultLogo:Int ? = null



    fun startCheckForUpdatedVersion(
        appSlug: String,
        appCurrentVersion: String,
        onCompletion: (Int) -> Unit
    ) {
        progressDialog?.show()
        currentActivity.get()?.lifecycleScope?.launch(Dispatchers.IO) {
            var urlConnection: URLConnection? = null
            try {
                val forceUpdateUrl = URL(GET_APP_VERSION + appSlug)
                urlConnection = forceUpdateUrl.openConnection()
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                urlConnection.setRequestProperty("Accept", "application/json")
                urlConnection?.connectTimeout = 20000
                urlConnection.readTimeout = 20000
                val inputStream = urlConnection.getInputStream()
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferReader = BufferedReader(inputStreamReader)
                val bufferedString = StringBuffer()
                var line: String? = null
                while ((bufferReader.readLine().also { line = it }) != null) {
                    bufferedString.append(line)
                }
                val jsonObject = JSONObject(bufferedString.toString())
                val appDetails = jsonObject.getJSONObject("app_detail")
                appVersionData = AppVersionData.getVersionObject(appDetails)
            } catch (e: Exception) {
                if (e is IOException) {
                    messageCode = ForceUpdateCodes.INTERNET_EXCEPTION
                } else {
                    messageCode = ForceUpdateCodes.SERVER_EXCEPTION
                }
                appVersionData = null
                Log.i("HTTP", e.localizedMessage)
                e.printStackTrace()
            }
        }?.invokeOnCompletion { throwable ->
            currentActivity.get()?.let {
                progressDialog?.dismiss()
                if (throwable == null && appVersionData != null) {
                    currentActivity.get()?.let {
                        it.lifecycleScope.launch(Dispatchers.Main) {
                            progressDialog?.dismiss()
                            if (!appVersionData!!.isAppRunnable(appCurrentVersion)) {
                                appVersionDialogFragment.alertContent = appVersionData!!
                                appVersionDialogFragment.setupUIColors(cardBackgroundColor,contentColor,buttonBackgroundColor,buttonTextColor,defaultLogo)
                                appVersionDialogFragment.show(it.supportFragmentManager, "")
                                messageCode = ForceUpdateCodes.VERSION_NOT_MATCHED
                            } else {
                                messageCode = ForceUpdateCodes.VERSION_MATCHED
                            }
                            onCompletion.invoke(messageCode)
                        }
                    }
                } else {
                    it.lifecycleScope.launch(Dispatchers.Main) {
                        onCompletion.invoke(messageCode)
                        currentActivity.clear()
                    }
                }
            }

        }

    }


    private fun getProgressDialog(context: Context?): AlertDialog? {
        context?.let {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.progress_dialog, null)
            dialogBuilder.setView(dialogView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.setCancelable(false)
            return alertDialog
        }
        return null

    }


}