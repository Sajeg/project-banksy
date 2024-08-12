package com.sajeg.banksy.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.sajeg.banksy.ImageDatabase
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader

private const val kModelFile = "damaged_helmet.glb"
private const val kMaxModelInstances = 10

@Composable
fun ARScreen(navController: NavController) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val modelInstances = remember { mutableListOf<ModelInstance>() }

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
            config.augmentedImageDatabase = ImageDatabase.getDatabase(session)
        },
        planeRenderer = true,
        onSessionCreated = { session ->

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
                            Toast.makeText(context, "TrackingObject", Toast.LENGTH_SHORT).show()
//                            createAnchorNode(engine, modelLoader, materialLoader, modelInstances, img.anchors.last())
                        }

                        AugmentedImage.TrackingMethod.NOT_TRACKING -> {
                            Log.d("ImageTracking", "Not Tracked")
                        }
                    }

                    // You can also check which image this is based on AugmentedImage.getName().
                    when (img.index) {
                        ImageDatabase.book -> Log.d(
                            "ObjectRecognition",
                            "recognized Object of type book"
                        )
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
    modelInstances: MutableList<ModelInstance>,
    anchor: Anchor
): AnchorNode {
    val anchorNode = AnchorNode(engine = engine, anchor = anchor)
    val modelNode = ModelNode(
        modelInstance = modelInstances.apply {
            if (isEmpty()) {
                this += modelLoader.createInstancedModel(kModelFile, kMaxModelInstances)
            }
        }[modelInstances.size-1],
        // Scale to fit in a 0.5 meters cube
        scaleToUnits = 0.5f
    ).apply {
        // Model Node needs to be editable for independent rotation from the anchor rotation
        isEditable = true
    }
    val boundingBoxNode = CubeNode(
        engine,
        size = modelNode.extents,
        center = modelNode.center,
        materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
        isVisible = false
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