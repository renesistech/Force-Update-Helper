package com.renesistech.appforceupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.renesistech.appupdate.ForceUpdateCodes
import com.renesistech.appupdate.RenesisForceUpdate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startUpdateCheck()
    }

    private fun startUpdateCheck() {
        val renesisForceUpdate = RenesisForceUpdate(this)
        /*     renesisForceUpdate.cardBackgroundColor=R.color.colorBlack
             renesisForceUpdate.buttonBackgroundColor=R.color.colorWhite
             renesisForceUpdate.buttonTextColor=R.color.colorBlack
             renesisForceUpdate.contentColor=R.color.colorWhite
             renesisForceUpdate.defaultLogo=R.drawable.ic_launcher_foreground*/
        renesisForceUpdate.startCheckForUpdatedVersion("com.akkastech.bmsu.bmsu","1.2.13") {
            when(it){
                ForceUpdateCodes.INTERNET_EXCEPTION->{
                    showMessage("Internet problem")
                }
                ForceUpdateCodes.SERVER_EXCEPTION->{
                    showMessage("Internal server error")
                }
                ForceUpdateCodes.VERSION_MATCHED->{
                    showMessage("Version  successfully matched")
                }
                ForceUpdateCodes.VERSION_NOT_MATCHED->{
                    showMessage("Version not matched")
                }
            }
        }
    }

    private fun showMessage(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}