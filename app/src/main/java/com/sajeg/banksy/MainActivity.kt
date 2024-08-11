package com.sajeg.banksy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import com.sajeg.banksy.ui.theme.ProjectBanksyTheme

class MainActivity : ComponentActivity() {
    private var arUserRequestedInstall = true
    var arSession: Session? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionAccepted ->
            Log.d("PermissionManager", permissionAccepted.toString())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectBanksyTheme {
                // This is for the navigation to work.
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)

                // Checks for camera permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }

                // Checks if ArCore Services are supported
                if (SessionManager.checkForARCoreSupport(this, this)) {
                    SessionManager.createSession(this)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SessionManager.destroySession()
    }
}
