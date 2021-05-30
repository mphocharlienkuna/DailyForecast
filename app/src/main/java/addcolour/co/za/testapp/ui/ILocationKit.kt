package addcolour.co.za.testapp.ui

import addcolour.co.za.testapp.BaseActivity
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.location.LocationSettingsResponse
import com.huawei.hms.location.LocationSettingsStatusCodes
import com.huawei.hms.location.SettingsClient

class ILocationKit @SuppressLint("MissingPermission")
private constructor(private val mContext: Context) : LiveData<Location>() {

    private val mFusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(mContext)

    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null)
                    value = location
            }
        }
    }

    init {
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
                value = location
        }
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()

        val task: Task<LocationSettingsResponse> = mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
        task.addOnSuccessListener {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper())
        }

//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                when ((exception as ApiException).statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                        (mContext as BaseActivity).showLocationSettings(exception)
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                        val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
//                        (mContext as BaseActivity).errorMessage(errorMessage)
//                    }
//                }
//            }
//        }
    }

    override fun onInactive() {
        super.onInactive()
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    companion object {

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        @SuppressLint("StaticFieldLeak")
        private var instance: ILocationKit? = null

        fun getInstance(appContext: Context): ILocationKit {
            if (instance == null) {
                instance = ILocationKit(appContext)
            }
            return instance as ILocationKit
        }
    }
}