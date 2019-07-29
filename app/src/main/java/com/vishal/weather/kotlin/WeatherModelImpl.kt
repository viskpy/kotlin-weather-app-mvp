package com.vishal.weather.kotlin

import android.content.Context

class WeatherModelImpl(context: Context) : WeatherContract.Model {
    override fun getInvalidCityMessage(): String {
        return context.getString(R.string.invalid_city)
    }

    var context: Context = context
}