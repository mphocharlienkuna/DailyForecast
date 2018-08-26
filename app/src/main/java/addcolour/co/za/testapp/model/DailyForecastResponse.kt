package addcolour.co.za.testapp.model

import com.google.gson.annotations.SerializedName

class DailyForecastResponse {

    @SerializedName("list")
    var forecastList: List<DailyForecast>? = null
}