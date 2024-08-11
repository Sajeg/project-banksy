package com.sajeg.banksy.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.ar.core.Config
import com.sajeg.banksy.ImageDatabase
import com.sajeg.banksy.MainActivity
import io.github.sceneview.ar.ARScene
import javax.net.ssl.CertPathTrustManagerParameters

@Composable
fun Home(navController: NavController) {
    ARScene(
        modifier = Modifier.fillMaxSize(),
        sessionConfiguration = { session, config ->
            config.depthMode =
                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.imageStabilizationMode = Config.ImageStabilizationMode.EIS
            config.augmentedImageDatabase = ImageDatabase.getDatabase(session)
        },
        planeRenderer = true,
        onSessionCreated = {session ->  

        },
        onSessionUpdated = { session, updatedFrame ->

        }
    )
}