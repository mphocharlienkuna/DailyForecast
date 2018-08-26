package addcolour.co.za.testapp.model

import com.google.gson.annotations.SerializedName

class ILocation(@SerializedName("lat") var latitude: Double, @SerializedName("lon") var longitude: Double)