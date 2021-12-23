package com.renesistech.appupdate

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat

object ForceUpdateCodes {
    val INTERNET_EXCEPTION = 0
    val SERVER_EXCEPTION = 1
    val VERSION_MATCHED = 2
    val VERSION_NOT_MATCHED = 3
}


fun Context.colorStateList(colorId: Int): ColorStateList? {
    return ContextCompat.getColorStateList(
        this, colorId
    )
}