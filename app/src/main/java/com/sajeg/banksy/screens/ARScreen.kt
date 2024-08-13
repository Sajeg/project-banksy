package com.sajeg.banksy.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.AugmentedImage
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.sajeg.banksy.ImageDatabase
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener

@Composable
fun ARScreen(navController: NavController) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    var frame by remember { mutableStateOf<Frame?>(null) }
    var planeRenderer by remember { mutableStateOf(true) }
    val childNodes = rememberNodes {
//        add(
//            ModelNode(
//                // Load it from a binary .glb in the asset files
//                modelInstance = modelLoader.createModelInstance(
//                    assetFileLocation = "model.glb"
//                ),
//                scaleToUnits = 1.0f
//            )
//        )

    }
    ARScene(
        engine = engine,
        modifier = Modifier.fillMaxSize(),
        planeRenderer = true,
        sessionConfiguration = { session, config ->
//            config.depthMode =
//                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                    true -> Config.DepthMode.AUTOMATIC
//                    else -> Config.DepthMode.DISABLED
//                }
//            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.geospatialMode = Config.GeospatialMode.ENABLED
            config.imageStabilizationMode = Config.ImageStabilizationMode.EIS
            config.augmentedImageDatabase = ImageDatabase.getDatabase(session, context)
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = { motionEvent, node ->
                if (node == null) {
                    val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
                    hitResults?.firstOrNull {
                        it.isValid(
                            depthPoint = false,
                            point = false
                        )
                    }?.createAnchorOrNull()
                        ?.let { anchor ->
                            planeRenderer = false
                            childNodes += createAnchorNode(
                                engine = engine,
                                modelLoader = modelLoader,
                                materialLoader = materialLoader,
                                anchor = anchor
                            )
                        }
                }
            }),
        onSessionUpdated = { session, updatedFrame ->
            frame = updatedFrame
            if (childNodes.isEmpty()) {
                updatedFrame.getUpdatedPlanes()
                    .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                        childNodes += createAnchorNode(
                            engine,
                            modelLoader,
                            materialLoader,
                            anchor
                        )
                    }
            }

            val augmentedImage = updatedFrame.getUpdatedAugmentedImages()
            for (img in augmentedImage) {
                if (img.trackingState == TrackingState.TRACKING) {

                    when (img.trackingMethod) {
                        AugmentedImage.TrackingMethod.LAST_KNOWN_POSE -> {}

                        AugmentedImage.TrackingMethod.FULL_TRACKING -> {
//                            Log.d("ImageTracking", "Is Tracking")
                            Toast.makeText(context, "TrackingObject", Toast.LENGTH_SHORT).show()
                        }

                        AugmentedImage.TrackingMethod.NOT_TRACKING -> {}
                    }
                }
            }
        }
    )
}

fun createAnchorNode(
    engine: Engine,
    modelLoader: ModelLoader,
    materialLoader: MaterialLoader,
    anchor: Anchor
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelLoader.createModelInstance("model.glb"),
        scaleToUnits = 0.5f
    ).apply {
        isEditable = true
        editableScaleRange = 0.2f..0.75f
    }
    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
    }
    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
        it.onEditingChanged = { editingTransforms ->
            boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
        }
    }
    return anchorNode
}