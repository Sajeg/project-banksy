package com.sajeg.banksy

import android.graphics.Bitmap

data class DatabaseImage(
    val name: String,
    val bitmap: Bitmap,
    var index: Int = -1
)
