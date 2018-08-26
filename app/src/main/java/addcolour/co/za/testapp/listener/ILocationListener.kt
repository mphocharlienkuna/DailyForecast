package addcolour.co.za.testapp.listener

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.content.Context
import android.location.Location

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException

import addcolour.co.za.testapp.BaseActivity
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class ILocationListener @SuppressLint("MissingPermission")
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
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                val statusCode = (exception as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        (mContext as BaseActivity).showLocationSettings(exception)
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                        (mContext as BaseActivity).errorMessage(errorMessage)
                    }
                }
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    companion object {

        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000

        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        @SuppressLint("StaticFieldLeak")
        private var instance: ILocationListener? = null

        fun getInstance(appContext: Context): ILocationListener {
            if (instance == null) {
                instance = ILocationListener(appContext)
            }
            return instance as ILocationListener
        }
    }
}