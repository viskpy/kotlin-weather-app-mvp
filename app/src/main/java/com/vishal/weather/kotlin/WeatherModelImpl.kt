package com.vishal.weather.kotlin

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import com.vishal.weather.kotlin.network.WeatherAPIClient
import com.vishal.weather.kotlin.network.WeatherRestService
import com.vishal.weather.kotlin.pojo.TemperatureResponse
import com.vishal.weather.kotlin.utils.FORECAST_DAYS
import com.vishal.weather.kotlin.utils.FORECAST_VIEW_DATE_FORMAT
import com.vishal.weather.kotlin.utils.INPUT_DATE_FORMAT
import io.reactivex.Observable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Model implementation for {@link WeatherActivity} that holds the data required by the
 * {@link WeatherPresenterImpl}.
 * Here we can define get the data from different sources like, SharedPref, LocalDB, Network call
 * and so on.
 *
 * @author Vishal - 31st July 2019
 * @since 1.0.0
 */
class WeatherModelImpl(context: Context) : WeatherContract.Model {

    var context: Context = context

    private val weatherRestService: WeatherRestService =
        WeatherAPIClient.getClient().create(WeatherRestService::class.java)

    /**
     * Initiates the API call for getting the weather info.
     *
     * @param cityName name of the city
     * @return {@link Observable<TemperatureResponse>}
     */
    override fun initiateWeatherInfoCall(textToBeSearched: String): Observable<TemperatureResponse> {
        return weatherRestService.getWeatherInfo(textToBeSearched, FORECAST_DAYS)
    }

    override fun fetchInvalidCityMessage(): String {
        return context.getString(R.string.invalid_city)
    }

    /**
     * Fetches the correct icon for weather condition based on condition code give by the API
     *
     * @param code of the condition type
     * @return icon to be used for weather condition
     */
    override fun getConditionIcon(code: Int?): Drawable? {
        var icon: Int = R.mipmap.clear;
        when (code) {
            1000 -> icon = R.mipmap.sun
            1003 -> icon = R.mipmap.clear
            1006 -> icon = R.mipmap.clouds
            1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201 -> icon = R.mipmap.rain
            1117 -> icon = R.mipmap.weather
        }
        return ContextCompat.getDrawable(context, icon)
    }

    /**
     * Calculates the display date from a give date inputDateString, uses the
     * FORECAST_VIEW_DATE_FORMAT for dates other that today and tomorrow.
     *
     * @param inputDateString date to be formatted
     * @return formatted date in FORECAST_VIEW_DATE_FORMAT
     */
    override fun getFormattedDate(inputDateString: String?): String? {
        val resources = context.resources
        val sdf = SimpleDateFormat(INPUT_DATE_FORMAT)
        try {
            val date = sdf.parse(inputDateString)
            val inputDate = Calendar.getInstance()
            inputDate.time = date
            val now = Calendar.getInstance()
            return when {
                now.get(Calendar.DATE) == inputDate.get(Calendar.DATE) -> resources.getString(R.string.today)
                inputDate.get(Calendar.DATE) - now.get(Calendar.DATE) == 1 -> resources.getString(R.string.tomorrow)
                else -> DateFormat.format(FORECAST_VIEW_DATE_FORMAT, inputDate).toString()
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

}