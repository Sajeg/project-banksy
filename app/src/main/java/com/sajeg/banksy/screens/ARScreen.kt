package com.sajeg.banksy.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.ar.core.Anchor
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.TrackingState
import com.sajeg.banksy.ImageDatabase
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.arcore.position
import io.github.sceneview.ar.arcore.rotation
import io.github.sceneview.math.Position
import io.github.sceneview.model.Model
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

@Composable
fun ARScreen(navController: NavController) {
    val context = LocalContext.current
    val childNodes = remember { mutableListOf<Node>() }
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    var model: Model? = null
    modelLoader.loadModelAsync("model.glb") {
        model = it
    }

    ARScene(
        engine = engine,
        modifier = Modifier.fillMaxSize(),
        sessionConfiguration = { session, config ->
//            config.depthMode =
//                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                    true -> Config.DepthMode.AUTOMATIC
//                    else -> Config.DepthMode.DISABLED
//                }
//            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.imageStabilizationMode = Config.ImageStabilizationMode.EIS
            config.augmentedImageDatabase = ImageDatabase.getDatabase(session, context)
        },
        planeRenderer = true,
        onSessionUpdated = { session, frame ->


            val augmentedImage = frame.getUpdatedAugmentedImages()
            for (img in augmentedImage) {
                if (img.trackingState == TrackingState.TRACKING) {

                    when (img.trackingMethod) {
                        AugmentedImage.TrackingMethod.LAST_KNOWN_POSE -> {}

                        AugmentedImage.TrackingMethod.FULL_TRACKING -> {
//                            Log.d("ImageTracking", "Is Tracking")
                            Toast.makeText(context, "TrackingObject", Toast.LENGTH_SHORT).show()
                            if (model != null) {
                                Log.d("ModelLoader", "model loaded")
                                addModelNode(img.createAnchor(img.centerPose), model!!, childNodes)
                            }
                        }

                        AugmentedImage.TrackingMethod.NOT_TRACKING -> {}
                    }
                }
            }
        }
    )
}

fun addModelNode(anchor: Anchor, model: Model, childNodes: MutableList<Node>): Node {
    Log.d("ModelLoader", "Showing the model")
    val modelNode = ModelNode(model.instance).apply {
        position = anchor.pose.position
        rotation = anchor.pose.rotation
        scaleToUnitCube(0.1f)
        centerOrigin(Position(x = 0f, y = 0f, z = 0f))
    }
    childNodes.add(modelNode)
    return modelNode
}