package addcolour.co.za.testapp.listener

import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

import addcolour.co.za.testapp.BaseActivity

class IPermissionListener(private val mBaseActivity: BaseActivity) : PermissionListener {

    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        mBaseActivity.onPermissionGranted(response.permissionName)
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        mBaseActivity.onPermissionDenied()
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest,
                                                    token: PermissionToken) {
    }
}