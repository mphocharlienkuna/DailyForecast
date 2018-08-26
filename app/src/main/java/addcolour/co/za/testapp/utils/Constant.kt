package addcolour.co.za.testapp.utils

import android.Manifest

interface Constant {
    companion object {

        const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION

        const val BASE_URL = "http://api.openweathermap.org"
        const val API_KEY = "53f9d8e4213222cf517d86dc406d67fc"

        const val UNIT = "metric"
    }
}