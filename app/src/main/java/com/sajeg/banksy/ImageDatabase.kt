package com.sajeg.banksy

import android.content.Context
import android.graphics.BitmapFactory
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Session
import java.io.InputStream

object ImageDatabase {
    private var database: AugmentedImageDatabase? = null
    var book = -1

    fun addImage(context: Context) {
        val bitmap = context.assets.open("image.jpg").use { BitmapFactory.decodeStream(it)}
        book = database!!.addImage("book", bitmap)
    }

    fun getDatabase(session: Session, context: Context): AugmentedImageDatabase {
        if (database == null) {
            database = AugmentedImageDatabase(session)
            addImage(context)
            return database!!
        } else {
            return database!!
        }
    }
}