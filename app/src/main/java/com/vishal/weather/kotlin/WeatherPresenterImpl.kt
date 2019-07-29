package com.vishal.weather.kotlin

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import org.apache.commons.lang3.StringUtils

class WeatherPresenterImpl(
    view: WeatherContract.View, model: WeatherContract.Model,
    processThread: Scheduler, mainThread: Scheduler
) : WeatherContract.Presenter {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var view: WeatherContract.View = view
    var model: WeatherContract.Model = model
    var processThread: Scheduler = processThread
    var mainThread: Scheduler = mainThread

    override fun init() {
        view.onInitView()
    }

    override fun getWeatherData(textToBeSearched: String) {
        if (!StringUtils.isBlank(textToBeSearched)) {
            view.handleLoaderView(true)
            view.handleWeatherView(false)
            view.handleErrorView(false)
//            compositeDisposable.add(model.initiateWeatherInfoCall(textToBeSearched))
        } else {
            view.showErrorMessage(model.getInvalidCityMessage());
        }
    }
}