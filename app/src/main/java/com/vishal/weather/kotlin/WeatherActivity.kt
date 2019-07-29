package com.vishal.weather.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.widget.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherActivity : AppCompatActivity(), WeatherContract.View {

    lateinit var loaderMainViewRL: RelativeLayout
    lateinit var loaderIV: ImageView
    lateinit var weatherForecastViewLL: LinearLayout
    lateinit var temperatureTV: TextView
    lateinit var cityNameTV: TextView
    lateinit var forecastRV: RecyclerView
    lateinit var errorViewRL: RelativeLayout
    lateinit var searchCityET: EditText

    lateinit var activity: WeatherActivity

    lateinit var presenter: WeatherContract.Presenter
    lateinit var model: WeatherContract.Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_main)
        model = WeatherModelImpl(activity)
        presenter = WeatherPresenterImpl(this, model, Schedulers.io(), AndroidSchedulers.mainThread())
        presenter.init()
        presenter.getWeatherData(getTextToBeSearched())
    }

    override fun onInitView() {
        loaderMainViewRL = findViewById(R.id.loader_main_view)
        loaderIV = findViewById(R.id.loader)
        weatherForecastViewLL = findViewById(R.id.weather_forecast_view)
        temperatureTV = findViewById(R.id.temperature)
        cityNameTV = findViewById(R.id.city_name)
        forecastRV = findViewById(R.id.forecast)
        errorViewRL = findViewById(R.id.retry_main_view)
        searchCityET = findViewById(R.id.search_city)

        errorViewRL.setOnClickListener {
            presenter.getWeatherData(getTextToBeSearched())
        }

        searchCityET.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= searchCityET.right - searchCityET.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    presenter.getWeatherData(getTextToBeSearched())
                    true
                }
            }
            false
        }
    }

    private fun getTextToBeSearched(): String {
        return searchCityET.text.toString()
    }

    override fun showErrorMessage(invalidCityMessage: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleLoaderView(showHandleLoader: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleWeatherView(showWeatherView: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleErrorView(showErrorView: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
