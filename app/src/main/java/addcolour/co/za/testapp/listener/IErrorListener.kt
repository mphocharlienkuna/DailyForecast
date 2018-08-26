package addcolour.co.za.testapp.listener

import android.util.Log

import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequestErrorListener

class IErrorListener : PermissionRequestErrorListener {
    override fun onError(error: DexterError) {
        Log.i(
                "TestApp",
                "There was an error: " + error.toString()
        )
    }
}