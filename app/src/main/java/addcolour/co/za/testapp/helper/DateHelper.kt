@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package addcolour.co.za.testapp.helper

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val DAY_OF_WEEK = "EEEE"
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun dateFormat(strDate: String): Long {
        val format = SimpleDateFormat(DATE_FORMAT, Locale.US)
        format.timeZone = TimeZone.getTimeZone("Etc/UTC")
        var date = Date()
        try {
            date = format.parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date.time
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayOfWeek(timeStamp: Long): String {
        val simpleDateFormat = SimpleDateFormat(DAY_OF_WEEK)
        val date = Date(timeStamp)
        return simpleDateFormat.format(date)
    }
}