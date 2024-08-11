package com.sajeg.banksy

import android.content.Context
import android.graphics.BitmapFactory
import com.google.ar.core.AugmentedImageDatabase
import java.io.InputStream

object ImageDatabase {
    private val database = AugmentedImageDatabase(MainActivity().arSession)

    fun addImage(context: Context) {
        val bitmap = context.assets.open("image.jpg").use { BitmapFactory.decodeStream(it)}
        database.addImage("book", bitmap)
    }

    fun getDatabase(): AugmentedImageDatabase {
        return database
    }
}