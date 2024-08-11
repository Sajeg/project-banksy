package com.sajeg.banksy.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.sajeg.banksy.MainActivity

@Composable
fun Home(navController: NavController) {
    if (MainActivity().arSession == null) {
        return
    }
    val session = MainActivity().arSession!!
    val config = Config(session)


}