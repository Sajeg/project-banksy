package com.sajeg.banksy.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.sajeg.banksy.ArScreen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Add Photos to Database")
        }
        Button(onClick = { navController.navigate(ArScreen) }) {
            Text(text = "TrackPhotos")
        }
    }
}