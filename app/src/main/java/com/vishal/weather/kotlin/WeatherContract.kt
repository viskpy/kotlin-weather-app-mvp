package com.vishal.weather.kotlin

import android.graphics.drawable.Drawable
import com.vishal.weather.kotlin.pojo.ForecastDataModel
import com.vishal.weather.kotlin.pojo.TemperatureResponse
import io.reactivex.Observable
import java.util.*

/**
 * Contract class for {@link WeatherActivity} that holds all the required method used by the View,
 * Model and Presenter.
 *
 * @author Vishal - 31st July 2019
 * @since 1.0.0
 */
interface WeatherContract {
    interface View {
        fun onInitView()
        fun showErrorMessage(invalidCityMessage: String?)
        fun handleLoaderView(showHandleLoader: Boolean)
        fun handleWeatherView(showWeatherView: Boolean)
        fun handleErrorView(showErrorView: Boolean)
        fun setCityCurrentTemperature(cityName: String?, temperature: String?)
        fun showForeCastData(forecastData: ArrayList<ForecastDataModel>)
    }

    interface Presenter {
        fun init()
        fun getWeatherData(textToBeSearched: String)
        fun destroyView()
        fun handleTemperatureResponse(temperatureResponse: TemperatureResponse?)
    }

    interface Model {
        fun fetchInvalidCityMessage(): String?
        fun initiateWeatherInfoCall(textToBeSearched: String): Observable<TemperatureResponse>
        fun getConditionIcon(code: Int?): Drawable?
        fun getFormattedDate(inputDateString: String?): String?
    }
}