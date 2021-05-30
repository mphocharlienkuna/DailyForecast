package addcolour.co.za.testapp.db.repo

import addcolour.co.za.testapp.app.Constant
import addcolour.co.za.testapp.model.DailyForecastResponse
import addcolour.co.za.testapp.network.ApiFactory.create
import addcolour.co.za.testapp.network.ApiInterface
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository private constructor() {

    private val mApiService: ApiInterface = create()

    private val mResponse = MutableLiveData<DailyForecastResponse?>()

    private val mErrorMessage = MutableLiveData<String>()

    fun loadSyncData(latitude: Double, longitude: Double) {
        mApiService.getForecast(latitude, longitude, Constant.API_KEY, Constant.UNIT)
                .enqueue(object : Callback<DailyForecastResponse?> {
                    override fun onResponse(call: Call<DailyForecastResponse?>, response: Response<DailyForecastResponse?>) {
                        if (response.isSuccessful && response.body() != null) {
                            mResponse.value = response.body()
                        } else {
                            mErrorMessage.value = Constant.ERROR_MESSAGE
                        }
                    }

                    override fun onFailure(call: Call<DailyForecastResponse?>, t: Throwable) {
                        mErrorMessage.value = Constant.ERROR_MESSAGE
                    }
                })
    }

    fun loadDailyForecast(): LiveData<DailyForecastResponse?> {
        return mResponse
    }

    fun errorMessage(): LiveData<String> {
        return mErrorMessage
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: WeatherRepository? = null

        @kotlin.jvm.JvmStatic
        fun getInstance(): WeatherRepository? {
            if (mInstance == null) mInstance = WeatherRepository()
            return mInstance
        }
    }
}