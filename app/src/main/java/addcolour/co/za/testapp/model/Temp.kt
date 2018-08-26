package addcolour.co.za.testapp.model

import com.google.gson.annotations.SerializedName

class Temp {
    @SerializedName("temp")
    var temp: Double = 0.toDouble()
    @SerializedName("temp_min")
    var temp_min: Double = 0.toDouble()
    @SerializedName("temp_max")
    var temp_max: Double = 0.toDouble()
    @SerializedName("pressure")
    var pressure: Double = 0.toDouble()
    @SerializedName("sea_level")
    var sea_level: Double = 0.toDouble()
    @SerializedName("grnd_level")
    var grnd_level: Double = 0.toDouble()
    @SerializedName("humidity")
    var humidity: Double = 0.toDouble()
    @SerializedName("temp_kf")
    var temp_kf: Double = 0.toDouble()
}