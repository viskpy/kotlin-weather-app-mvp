package com.vishal.weather.kotlin.network

import com.vishal.weather.kotlin.utils.DEFAULT_CONNECT_TIMEOUT_IN_MS
import com.vishal.weather.kotlin.utils.DEFAULT_READ_TIMEOUT_IN_MS
import com.vishal.weather.kotlin.utils.DEFAULT_WRITE_TIMEOUT_IN_MS
import com.vishal.weather.kotlin.utils.END_POINT
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This object class holds the configuration for calling weather APIS, end point and other timeouts are
 * defined here. And common header can be defined as a interceptor in
 * {@link WeatherAPIClient#getClient()}
 *
 * @author Vishal - 31st August 2019
 * @since 1.0.0
 */
object WeatherAPIClient {
    lateinit var retrofit: Retrofit

    /**
     * Binds and returns the retorfit client object. We declares global headers, logger and other
     * required interceptors.
     *
     * @return wrapped configuration object of type {@link Retrofit}
     */
    fun getClient(): Retrofit {
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val oktHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
        oktHttpClient.addInterceptor(logging)
        oktHttpClient.addInterceptor { chain ->
            var original: Request = chain.request()
            var request: Request = original.newBuilder()
                .method(original.method(), original.body())
                .build()
            chain.proceed(request)
        }
        if (!::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(END_POINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build()
        }
        return retrofit
    }
}