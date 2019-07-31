package com.vishal.weather.kotlin.network

import com.vishal.weather.kotlin.utils.API_KEY
import com.vishal.weather.kotlin.pojo.TemperatureResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This interface is responsible for holding all APIs need to be invoked by the application.
 *
 * @author Vishal - 31st August 2019
 * @since 1.0.0
 */
interface WeatherRestService {
    /**
     * Returns weather info response which will be observed by the callee.
     *
     * @param cityName name of the city to be searched
     * @param days     no. of days for forecast data
     * @return {@link Observable<TemperatureResponse>}
     */
    @GET("v1/forecast.json?$API_KEY")
    fun getWeatherInfo(@Query("q") cityName: String, @Query("days") days: String): Observable<TemperatureResponse>
}