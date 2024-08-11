package com.sajeg.banksy

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.ar.core.ArCoreApk
import com.google.ar.core.ArCoreApk.InstallStatus
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException

object SessionManager {
    private var session: Session? = null
    private var config: Config? = null

    fun checkForARCoreSupport(context: Context, activity: Activity): Boolean {
        return when (ArCoreApk.getInstance().checkAvailability(context)) {
            ArCoreApk.Availability.SUPPORTED_INSTALLED -> return true
            ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED, ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD -> {
                try {
                    when (ArCoreApk.getInstance().requestInstall(activity, true)) {
                        InstallStatus.INSTALL_REQUESTED -> false
                        InstallStatus.INSTALLED -> true
                    }
                } catch (e: UnavailableException) {
                    Log.e("SessionManager", "ARCore not installed", e)
                    false
                }
            }
            ArCoreApk.Availability.UNKNOWN_ERROR -> false
            ArCoreApk.Availability.UNKNOWN_CHECKING -> false
            ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> false
            ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
                Toast.makeText(context, "Device unsupported", Toast.LENGTH_LONG)
                false
            }
        }
    }

    fun createSession(context: Context, thisSession: Session) {
        session = Session(context)
        config = Config(session)
        config!!.augmentedImageDatabase = ImageDatabase.getDatabase(thisSession)
        session!!.configure(config)
    }

    fun destroySession() {
        if (session != null) {
            session!!.close()
        }
    }

    fun getSession() : Session?{
        return session
    }
}