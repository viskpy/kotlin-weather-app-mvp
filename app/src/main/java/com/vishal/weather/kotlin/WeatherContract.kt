package com.vishal.weather.kotlin

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface WeatherContract {
    interface View {
        fun onInitView()
        fun showErrorMessage(invalidCityMessage: String)
        fun handleLoaderView(showHandleLoader: Boolean)
        fun handleWeatherView(showWeatherView: Boolean)
        fun handleErrorView(showErrorView: Boolean)
    }

    interface Presenter {
        fun init()
        fun getWeatherData(textToBeSearched: String)
    }

    interface Model {
        fun getInvalidCityMessage(): String
        fun initiateWeatherInfoCall(textToBeSearched: String): Observable<T>

    }
}