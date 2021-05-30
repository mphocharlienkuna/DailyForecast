package addcolour.co.za.testapp.network

import addcolour.co.za.testapp.app.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private var retrofit: Retrofit? = null

     fun create(): ApiInterface {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!.create(ApiInterface::class.java)
    }
}