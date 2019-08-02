package com.vishal.weather.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.*
import com.vishal.weather.kotlin.pojo.ForecastDataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * This activity will be the first activity to be shown to user. Initially a leader view
 * {@link WeatherActivity#loaderMainViewRL} will be shown to user unless weather data is fetched.
 * For now Bangalore is added the default city to be shown and with the help of view
 * {@link WeatherActivity#searchCityET} user can search city for checking the weather and forecast.
 * ================================================================================================
 * This class follows MVP design pattern, where Model, View and Presenter's contract is commonly
 * defined in {@link WeatherContract} class. And implementations are defined in
 * {@link WeatherModelImpl} and {@link WeatherPresenterImpl} classes.
 *
 * @author Vishal - 31st July 2019
 * @since 1.0.0
 */
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

    /**
     * Callback give from {@link WeatherPresenterImpl} for initializing the views and there
     * listeners if they have any.
     */
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

    /**
     * Handles the visibility of loader view.
     *
     * @param show show the view if true else hide it.
     */
    override fun handleLoaderView(showHandleLoader: Boolean) {
        if (showHandleLoader) {
            val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
            rotation.repeatCount = Animation.INFINITE
            rotation.repeatMode = Animation.RESTART
            loaderIV.startAnimation(rotation)
            loaderMainViewRL.visibility = View.VISIBLE
        } else {
            loaderMainViewRL.visibility = View.GONE
        }
    }

    /**
     * Handles the visibility of weather view.
     *
     * @param show show the view if true else hide it.
     */
    override fun handleWeatherView(showWeatherView: Boolean) {
        weatherForecastViewLL.visibility = if (showWeatherView) View.VISIBLE else View.GONE
    }

    /**
     * Handles the visibility of error view.
     *
     * @param show show the view if true else hide it.
     */
    override fun handleErrorView(showErrorView: Boolean) {
        errorViewRL.visibility = if (showErrorView) View.VISIBLE else View.GONE
    }

    /**
     * A common method for showing any message in Toast format.
     *
     * @param message to be shown
     */
    override fun showErrorMessage(invalidCityMessage: String?) {
        Toast.makeText(activity, invalidCityMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * Sets the city name and temperature of that city.
     *
     * @param cityName    name of the city.
     * @param temperature of the city.
     */
    override fun setCityCurrentTemperature(cityName: String?, temperature: String?) {
        cityNameTV.text = cityName
        temperatureTV.text = temperature
    }

    /**
     * Displays the forecast information based on searched city. It also applies a slide up
     * animation for all forecast rows.
     *
     * @param forecastData
     */
    override fun showForeCastData(forecastData: ArrayList<ForecastDataModel>) {
        forecastRV.layoutManager = LinearLayoutManager(activity)
        forecastRV.adapter = ForecastAdapter(forecastData)
        val controller: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(activity, R.anim.aslide_up_anim)
        forecastRV.layoutAnimation = controller
        (forecastRV.adapter as ForecastAdapter).notifyDataSetChanged()
        forecastRV.scheduleLayoutAnimation()
    }

    /**
     * @return text value of {@link WeatherActivity#searchCityET}
     */
    private fun getTextToBeSearched(): String {
        return searchCityET.text.toString()
    }

    /**
     * Callback method before activity destroys
     */
    override fun finish() {
        super.finish()
        presenter.destroyView()
    }
}
