package com.sajeg.banksy.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.sajeg.banksy.ImageDatabase
import com.sajeg.banksy.MainActivity

@Composable
fun Home(navController: NavController) {
    if (MainActivity().arSession == null) {
        return
    }
    val session = MainActivity().arSession!!
    val config = Config(session)

//    config.augmentedImageDatabase = ImageDatabase.getDatabase()
//    session.configure(config)
//    Text(text = "Hi")
}