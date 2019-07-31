package com.vishal.weather.kotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.vishal.weather.kotlin.pojo.ForecastDataModel

/**
 * Adapter class for showing list of forecast of a give city. This class expect a constructor data
 * in the form of {@link List<ForecastDataModel>} which holds all forecast data.
 *
 * @author Vishal - 31st August 2019
 * @since 1.0.0
 */
class ForecastAdapter constructor(forecastData: List<ForecastDataModel>) : RecyclerView.Adapter<ForecastAdapter
.ForecastHolder>() {
    private val forecastData: List<ForecastDataModel> = forecastData

    class ForecastHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dayNameTV: TextView = view.findViewById(R.id.day_name)
        private val conditionTextTV: TextView = view.findViewById(R.id.condition_text)
        private val minMaxTempTV: TextView = view.findViewById(R.id.min_max_temp)
        private val conditionIconIV: ImageView = view.findViewById(R.id.condition_icon)

        fun setForecastDataModel(forecastDataModel: ForecastDataModel) {
            dayNameTV.text = forecastDataModel.day
            conditionTextTV.text = forecastDataModel.condition
            minMaxTempTV.text = forecastDataModel.minMaxTemp
            conditionIconIV.setImageDrawable(forecastDataModel.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_row, parent, false)
        return ForecastHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastData.size
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.setForecastDataModel(forecastData[position])
    }
}