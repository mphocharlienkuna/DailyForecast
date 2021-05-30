package addcolour.co.za.testapp.ui.viewmodel

import addcolour.co.za.testapp.db.repo.WeatherRepository
import addcolour.co.za.testapp.db.repo.WeatherRepository.Companion.getInstance
import addcolour.co.za.testapp.model.DailyForecastResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: WeatherRepository? = getInstance()

    fun loadSyncData(latitude: Double, longitude: Double) {
        mRepository!!.loadSyncData(latitude, longitude)
    }

    fun loadDailyForecast(): LiveData<DailyForecastResponse?> {
        return mRepository!!.loadDailyForecast()
    }

    fun errorMessage(): LiveData<String> {
        return mRepository!!.errorMessage()
    }
}