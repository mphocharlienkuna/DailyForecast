package addcolour.co.za.testapp.ui

import addcolour.co.za.testapp.BaseActivity
import addcolour.co.za.testapp.R
import addcolour.co.za.testapp.app.Constant
import addcolour.co.za.testapp.databinding.ActivityMainBinding
import addcolour.co.za.testapp.helper.DateHelper.dateFormat
import addcolour.co.za.testapp.helper.DateHelper.getDayOfWeek
import addcolour.co.za.testapp.model.DailyForecastResponse
import addcolour.co.za.testapp.model.WeatherList
import addcolour.co.za.testapp.ui.ILocationListener.Companion.getInstance
import addcolour.co.za.testapp.ui.adapter.WeatherAdapter
import addcolour.co.za.testapp.ui.viewmodel.ConnectivityLiveData
import addcolour.co.za.testapp.ui.viewmodel.WeatherViewModel
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.util.*

class MainActivity : BaseActivity() {
    private var mBinding: ActivityMainBinding? = null
    private var mConnectivityLiveData: ConnectivityLiveData? = null
    private var isNetworkAvailable = false
    private var mViewModel: WeatherViewModel? = null
    private var mAdapter: WeatherAdapter? = null
    private val mList: MutableList<WeatherList> = ArrayList()
    private var mDayOfWeek = "Today"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mConnectivityLiveData = ConnectivityLiveData(this.application)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding?.lifecycleOwner = this

        initialize()
    }

    private fun initialize() {
        requestPermission(Constant.LOCATION_PERMISSION)
        mAdapter = WeatherAdapter()
        mAdapter!!.setList(mList)
        mBinding!!.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        mViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        mViewModel!!.loadDailyForecast().observe(this, { dailyForecastResponse: DailyForecastResponse? ->
            if (dailyForecastResponse?.forecastList != null) {
                mList.clear()
                for (forecast in dailyForecastResponse.forecastList!!) {
                    if (forecast.dtTxt != null) {
                        if (mDayOfWeek != getDayOfWeek(dateFormat(forecast.dtTxt!!))) {
                            mDayOfWeek = getDayOfWeek(dateFormat(forecast.dtTxt!!))
                            mList.add(WeatherList(mDayOfWeek, forecast.weather!![0].main!!, forecast.temp!!.temp_min, forecast.temp!!.temp_max, forecast.temp!!.temp_max))
                        }
                    }
                }
                mBinding!!.isProgressBar = true
                mBinding!!.data = mList[0]
                mAdapter!!.setList(mList)
            }
        })
        mViewModel!!.errorMessage().observe(this, { errorMessage: String ->
            if (errorMessage.isNotEmpty()) {
                mBinding!!.contentError.textViewNoData.text = errorMessage
                mBinding!!.noData = true
            }
        })
    }

    override fun onPermissionGranted(permissionName: String) {
        if (Constant.LOCATION_PERMISSION == permissionName) {
            mConnectivityLiveData!!.observe(this, { isAvailable: Boolean ->
                isNetworkAvailable = isAvailable
                if (isNetworkAvailable) {
                    dismissSnackBar()
                    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this, 13000000) != ConnectionResult.SUCCESS) {
                        if (Build.MANUFACTURER.equals(Constant.BUILD_MANUFACTURER, ignoreCase = true)) {
                            performLocationKit()
                        } else performAndroidLocationUpdate()
                    } else performAndroidLocationUpdate()
                } else {
                    showSnackBar(R.string.error_no_connection,
                            R.string.btn_retry) { }
                }
            })
        }
    }

    private fun performLocationKit() {
        ILocationKit.getInstance(this).observe(this, {location: Location? ->
            if(location != null){
                mBinding!!.isLocation = isNetworkAvailable
                loadSyncData(location.latitude, location.longitude)
            }
        })
    }

    private fun performAndroidLocationUpdate() {
        getInstance(this).observe(this, { location: Location? ->
            if (location != null) {
                mBinding!!.isLocation = isNetworkAvailable
                loadSyncData(location.latitude, location.longitude)
            }
        })
    }

    private fun loadSyncData(latitude: Double, longitude: Double) {
        mViewModel!!.loadSyncData(latitude, longitude)
    }
}