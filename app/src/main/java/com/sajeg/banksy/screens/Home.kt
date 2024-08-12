package com.sajeg.banksy.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.TrackingState
import com.sajeg.banksy.ImageDatabase
import com.sajeg.banksy.MainActivity
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import javax.net.ssl.CertPathTrustManagerParameters

@Composable
fun Home(navController: NavController) {
    val context = LocalContext.current
    ARScene(
        modifier = Modifier.fillMaxSize(),
        sessionConfiguration = { session, config ->
//            config.depthMode =
//                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                    true -> Config.DepthMode.AUTOMATIC
//                    else -> Config.DepthMode.DISABLED
//                }
//            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
//            config.imageStabilizationMode = Config.ImageStabilizationMode.EIS
            config.augmentedImageDatabase = ImageDatabase.getDatabase(session, context)
        },
        planeRenderer = true,
        onSessionCreated = {session ->  

        },
        onSessionUpdated = { session, updatedFrame ->
            val augmentedImage = updatedFrame.getUpdatedAugmentedImages()
            for (img in augmentedImage) {
                if (img.trackingState == TrackingState.TRACKING) {

                    when (img.trackingMethod) {
                        AugmentedImage.TrackingMethod.LAST_KNOWN_POSE -> {
                            Log.d("ImageTracking", "Known Pose")
                        }
                        AugmentedImage.TrackingMethod.FULL_TRACKING -> {
                            Log.d("ImageTracking", "Is Tracking")
                        }
                        AugmentedImage.TrackingMethod.NOT_TRACKING -> {
                            Log.d("ImageTracking", "Not Tracked")
                        }
                    }

                    // You can also check which image this is based on AugmentedImage.getName().
                    when (img.index) {
                        ImageDatabase.book -> Log.d("ObjectRecognition", "recognized Object of type book")
                    }
                }
            }
        }
    )
}