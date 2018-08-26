package addcolour.co.za.testapp

import android.content.IntentSender
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

import com.google.android.gms.common.api.ResolvableApiException
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener

import addcolour.co.za.testapp.listener.IErrorListener
import addcolour.co.za.testapp.listener.IPermissionListener

abstract class BaseActivity : AppCompatActivity() {

    private var mPermissionListener: PermissionListener? = null
    private var errorListener: PermissionRequestErrorListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPermissionListeners()
    }

    abstract fun onPermissionGranted(permissionName: String);

    fun requestPermission(permission: String) {
        Dexter.withActivity(this)
                .withPermission(permission)
                .withListener(mPermissionListener)
                .withErrorListener(errorListener)
                .check()
    }

    private fun createPermissionListeners() {
        val feedbackViewPermissionListener = IPermissionListener(this)
        val dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(this)
                .withTitle(R.string.location_permission_denied_dialog_title)
                .withMessage(R.string.location_permission_denied_dialog_feedback)
                .withButtonText(android.R.string.ok)
                .build()

        mPermissionListener = CompositePermissionListener(feedbackViewPermissionListener,
                dialogOnDeniedPermissionListener)
        errorListener = IErrorListener()
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

    fun errorMessage(errorMessage: String) {
        Log.e(TAG, errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    companion object {

        private val TAG = BaseActivity::class.java.simpleName

        private const val REQUEST_CHECK_SETTINGS = 0x1
    }
}