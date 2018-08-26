package addcolour.co.za.testapp.model

import com.google.gson.annotations.SerializedName

class DailyForecast {

    @SerializedName("dt")
    var dt: Long = 0
    @SerializedName("main")
    var temp: Temp? = null
    @SerializedName("weather")
    var weather: List<Weather>? = null
    @SerializedName("dt_txt")
    var dtTxt: String? = null
}