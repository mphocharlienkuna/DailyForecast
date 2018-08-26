package addcolour.co.za.testapp.model

import com.google.gson.annotations.SerializedName

class ILocation {

    @SerializedName("lat")
    var latitude: Double = 0.toDouble()
    @SerializedName("lon")
    var longitude: Double = 0.toDouble()

    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }
}