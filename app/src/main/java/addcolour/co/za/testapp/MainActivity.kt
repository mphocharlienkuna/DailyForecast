package addcolour.co.za.testapp

import addcolour.co.za.testapp.adapter.WeatherAdapter
import addcolour.co.za.testapp.app.AppController
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

import addcolour.co.za.testapp.databinding.ActivityMainBinding
import addcolour.co.za.testapp.helper.DateHelper
import addcolour.co.za.testapp.listener.ILocationListener
import addcolour.co.za.testapp.model.ILocation
import addcolour.co.za.testapp.model.WeatherList
import addcolour.co.za.testapp.utils.Constant
import android.app.PendingIntent.getActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {

    private var mBinding: ActivityMainBinding? = null

    private lateinit var weatherAdapter: WeatherAdapter
    private var manager: LinearLayoutManager? = null

    private var dayOfWeek: String = "Today"
    private var mList: MutableList<WeatherList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initDataBinding()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnRetry -> initDataBinding()
        }
    }

    private fun initDataBinding() {

        requestPermission(Constant.LOCATION_PERMISSION)
        manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding!!.recyclerView.layoutManager = manager

        weatherAdapter = WeatherAdapter()
        mBinding!!.recyclerView.adapter = weatherAdapter
        (mBinding!!.recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        mBinding!!.noData = false
        mBinding!!.isLocation = false
        mBinding!!.isProgressBar = false

        mBinding!!.contentError!!.btnRetry.setOnClickListener(this)
    }

    override fun onPermissionGranted(permissionName: String) {
        if (Constant.LOCATION_PERMISSION == permissionName) {
            ILocationListener.getInstance(this)
                    .observe(this, Observer { location ->
                        if (location != null) {
                            mBinding!!.isLocation = true

                            Log.i(TAG,
                                    "Location Changed " + location.latitude + " : " + location.longitude)
                            loadSyncData(ILocation(location.latitude, location.longitude))
                        }
                    })
        }
    }

    private fun loadSyncData(location: ILocation) {
        val appController = AppController.create(this)
        val apiInterface = appController.apiInterface
        apiInterface!!.getForecast(location.latitude, location.longitude, Constant.API_KEY, Constant.UNIT)
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ forecastResponse ->
                    if (forecastResponse.forecastList != null) {
                        mList = ArrayList()

                        for (response in forecastResponse.forecastList!!) if (dayOfWeek != DateHelper.getDayOfWeek(DateHelper.dateFormat(response.dtTxt!!))) {

                            dayOfWeek = DateHelper.getDayOfWeek(DateHelper.dateFormat(response.dtTxt!!))
                            (mList as ArrayList<WeatherList>).add(WeatherList(dayOfWeek, response.weather!![0].main!!, response.temp!!.temp_min
                                    , response.temp!!.temp_max, response.temp!!.temp_max))
                        }

                        mBinding!!.data = (mList as ArrayList<WeatherList>)[0]
                        (mList as ArrayList<WeatherList>).removeAt(0)

                        mBinding!!.isProgressBar = true
                        weatherAdapter.setWeatherListList(mList as ArrayList<WeatherList>)
                    }
                }) {
                    mBinding!!.noData = true
                }
    }

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }
}