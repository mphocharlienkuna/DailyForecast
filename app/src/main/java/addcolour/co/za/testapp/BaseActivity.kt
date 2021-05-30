package addcolour.co.za.testapp

import addcolour.co.za.testapp.app.Constant
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.snackbar.Snackbar
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    private var mSnackBar: Snackbar? = null
    abstract fun onPermissionGranted(permissionName: String)

    private val permissionHandler = object : PermissionHandler() {
        override fun onGranted() {
            onPermissionGranted(Constant.LOCATION_PERMISSION)
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            onPermissionDenied()
        }

        override fun onJustBlocked(context: Context?, justBlockedList: ArrayList<String>?, deniedPermissions: ArrayList<String>?) {
            onPermissionDenied()
        }
    }

    fun requestPermission(permission: String) {
        Permissions.check(this, permission, null, permissionHandler)
    }

    fun onPermissionDenied() {
        showSnackBar(R.string.permission_denied_explanation,
                R.string.settings) {
            // check for permanent denial of any permission
            openSettings()
        }
    }

    fun showLocationSettings(rae: ResolvableApiException) {
        try {
            // Show the dialog by calling startResolutionForResult(), and check the
            // result in onActivityResult().
            rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
        } catch (sie: IntentSender.SendIntentException) {
            Log.i(TAG, "PendingIntent unable to execute request.")
        }
    }

    private fun openSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }

    fun errorMessage(errorMessage: String) {
        Log.e(TAG, errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    fun showSnackBar(mainTextStringId: Int, actionStringId: Int,
                     listener: View.OnClickListener) {
        mSnackBar = Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
        mSnackBar?.show()
    }

    fun dismissSnackBar() {
        mSnackBar?.dismiss()
    }

    companion object {
        private val TAG = BaseActivity::class.java.simpleName
        private const val REQUEST_CHECK_SETTINGS = 0x1
    }
}