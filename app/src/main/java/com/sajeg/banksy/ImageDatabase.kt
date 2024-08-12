package com.sajeg.banksy

import android.content.Context
import android.graphics.BitmapFactory
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Session

object ImageDatabase {
    private var database: AugmentedImageDatabase? = null
    var images = mutableListOf<DatabaseImage>()
    var book = -1

    fun addImage(context: Context,) {
        val bitmap = context.assets.open("image.jpg").use { BitmapFactory.decodeStream(it)}
        book = database!!.addImage("book", bitmap)
    }

    fun getDatabase(session: Session, context: Context): AugmentedImageDatabase {
        if (database == null) {
            database = AugmentedImageDatabase(session)
            for (img in images) {
                img.index = database!!.addImage(img.name, img.bitmap)
            }
            addImage(context)
            return database!!
        } else {
            return database!!
        }
    }
}