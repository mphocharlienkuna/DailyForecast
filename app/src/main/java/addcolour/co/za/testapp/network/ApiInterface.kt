package addcolour.co.za.testapp.network

import addcolour.co.za.testapp.model.DailyForecastResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/data/2.5/forecast")
    fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double
                    , @Query("APPID") appID: String, @Query("units") unit: String)
            : Call<DailyForecastResponse>
}