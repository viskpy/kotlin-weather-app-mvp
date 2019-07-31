package com.vishal.weather.kotlin

import android.graphics.drawable.Drawable
import com.google.gson.Gson
import com.vishal.weather.kotlin.pojo.ForecastDataModel
import com.vishal.weather.kotlin.pojo.TemperatureResponse
import com.vishal.weather.kotlin.utils.addDegreeSymbol
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import org.apache.commons.lang3.StringUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.*

/**
 * Presenter implementation for {@link WeatherActivity} that holds the view's command and fetches
 * data from {@link WeatherModelImpl}, then updates the View UI.
 *
 * @author Vishal - 31st August 2019
 * @since 1.0.0
 */
class WeatherPresenterImpl(
    view: WeatherContract.View, model: WeatherContract.Model,
    processThread: Scheduler, mainThread: Scheduler
) : WeatherContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var view: WeatherContract.View = view
    var model: WeatherContract.Model = model
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread
    /**
     * Initializes the basic UI based on requirement.
     */
    override fun init() {
        view.onInitView()
    }

    /**
     * Uses the RXJava for doing the API calls and handling the response. It updates the ui based
     * on error or success response.
     *
     * @param cityName name of the city
     */
    override fun getWeatherData(textToBeSearched: String) {
        if (!StringUtils.isBlank(textToBeSearched)) {
            view.handleLoaderView(true)
            view.handleWeatherView(false)
            view.handleErrorView(false)
            compositeDisposable.add(
                model.initiateWeatherInfoCall(textToBeSearched).subscribeOn(processThread).observeOn(
                    mainThread
                ).subscribeWith(object : DisposableObserver<TemperatureResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(temperatureResponse: TemperatureResponse) {
                        handleTemperatureResponse(temperatureResponse)
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            try {
                                val body = e.response().errorBody()!!.string()
                                handleTemperatureResponse(
                                    Gson().fromJson(
                                        body, TemperatureResponse::class.java
                                    )
                                )
                            } catch (e1: IOException) {
                                e1.printStackTrace()
                            }

                        } else {
                            view.handleErrorView(true)
                        }
                    }

                })
            )
        } else {
            view.showErrorMessage(model.fetchInvalidCityMessage());
        }
    }

    /**
     * Handles the response of weather info api, and updates the city name, temperature and
     * forecast view.
     *
     * @param temperatureResponse
     */
    override fun handleTemperatureResponse(temperatureResponse: TemperatureResponse) {
        if (null != temperatureResponse) {
            val error = temperatureResponse.error
            if (null != error) {
                view.showErrorMessage(error.message)
                view.handleLoaderView(false)
                view.handleWeatherView(true)
            } else {
                val location = temperatureResponse.location
                val current = temperatureResponse.current
                if (null != location && null != current) {
                    val cityName = location.name
                    val temperature = current.tempC
                    if (!StringUtils.isBlank(cityName) && null != temperature) {
                        view.handleLoaderView(false)
                        view.handleErrorView(false)
                        view.handleWeatherView(true)
                        view.setCityCurrentTemperature(
                            cityName,
                            addDegreeSymbol(temperature)
                        )
                    } else {
                        showErrorView()
                    }
                }
                val forecast = temperatureResponse.forecast
                if (null != forecast) {
                    val forecastData = ArrayList<ForecastDataModel>()
                    val allForeCast = forecast.forecastday
                    if (null != allForeCast) {
                        allForeCast.forEach { forecastDay ->
                            var day: String? = ""
                            var condition: String? = ""
                            var conditionIcon: Drawable? = null
                            var minMaxTemp = ""
                            val dayDetails = forecastDay.day
                            if (null != dayDetails) {
                                val minTemp = dayDetails.mintempC
                                val maxTemp = dayDetails.maxtempC
                                day = model.getFormattedDate(forecastDay.date)
                                val conditions = dayDetails.condition
                                if (null != conditions) {
                                    condition = conditions.text
                                    conditionIcon = model.getConditionIcon(conditions.code)
                                }
                                if (null != minTemp && null != maxTemp) {
                                    minMaxTemp = "${addDegreeSymbol(minTemp)} / ${addDegreeSymbol(
                                        maxTemp
                                    )}"
                                }
                                forecastData.add(
                                    ForecastDataModel(
                                        day, condition, conditionIcon,
                                        minMaxTemp
                                    )
                                )
                            }
                        }
                    }
                    if (forecastData.size > 0) {
                        view.showForeCastData(forecastData)
                    }
                }
            }
        } else {
            view.handleErrorView(true)
        }
    }

    /**
     * Manages the view based on error.
     */
    private fun showErrorView() {
        view.handleLoaderView(false)
        view.handleErrorView(true)
    }

    /**
     * This call back is give by the Activity while finishing, observables used in this presenter
     * will be disposed here.
     */
    override fun destroyView() {
        compositeDisposable.dispose()
    }
}