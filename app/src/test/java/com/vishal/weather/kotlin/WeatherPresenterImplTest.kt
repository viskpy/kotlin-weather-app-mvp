package com.vishal.weather.kotlin

import android.content.Context
import android.content.res.Resources
import android.text.format.DateFormat
import com.vishal.weather.kotlin.pojo.Current
import com.vishal.weather.kotlin.pojo.Error
import com.vishal.weather.kotlin.pojo.Location
import com.vishal.weather.kotlin.pojo.TemperatureResponse
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.apache.commons.lang3.StringUtils
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

/**
 * Test class for {@link WeatherPresenterImpl}. {@link PowerMockito} is used for mocking the
 * static methods.
 *
 * @author Vishal - 1st August 2019
 * @since 1.0.0
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(StringUtils::class, DateFormat::class)
@PowerMockIgnore("javax.net.ssl.*")
class WeatherPresenterImplTest {

    private lateinit var newObservable: Observable<TemperatureResponse>
    private lateinit var model: WeatherContract.Model
    @Mock
    private lateinit var view: WeatherContract.View
    private lateinit var presenter: WeatherContract.Presenter
    private lateinit var testScheduler: TestScheduler
    @Mock
    private lateinit var context: Context
    @Mock
    private var resources: Resources? = null
    @Mock
    private var temperatureResponse: TemperatureResponse? = null
    @Mock
    private var error: Error? = null
    @Mock
    private var location: Location? = null
    @Mock
    private var current: Current? = null
    private var errorText = "Something went wrong"

    /**
     * Basic setup is done here, like creating supported objects or intercepting ny values of a
     * method call.
     */
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(StringUtils::class.java)
        PowerMockito.mockStatic(DateFormat::class.java)
        testScheduler = TestScheduler()
        model = WeatherModelImpl(context)
        presenter = WeatherPresenterImpl(view, model, testScheduler, testScheduler)
        PowerMockito.`when`(StringUtils.isEmpty(any<CharSequence>(CharSequence::class.java)))
            .thenAnswer {
                val charData = it.arguments[0] as CharSequence
                ;!(charData != null && charData.isNotEmpty())
            }
    }

    @Test
    fun initView() {
        presenter.init()
        Mockito.verify(view).onInitView()
        Mockito.verify(view).handleWeatherView(false)
        Mockito.verify(view).handleErrorView(false)
    }

    @Test
    fun getWeatherData() {
        PowerMockito.`when`(DateFormat.format(any(CharSequence::class.java), any(Calendar::class.java)))
            .thenReturn("25 Feb 2019")
        presenter.getWeatherData("")
        Mockito.verify(view).showErrorMessage(model.fetchInvalidCityMessage())
        val inputData = TemperatureResponse()
        newObservable = Observable.just(inputData)
        Mockito.`when`(context.resources).thenReturn(resources)

        presenter.getWeatherData("Delhi")

        testScheduler.triggerActions()
        val testObserver = TestObserver.create<Any>()
        newObservable.subscribe(testObserver)
        testObserver.assertNoErrors()
        testObserver.assertSubscribed()
        testObserver.assertComplete()

        //API call test
        val responseTestObserver = TestObserver.create<TemperatureResponse>()
        model.initiateWeatherInfoCall("Patna").subscribe(responseTestObserver)
        responseTestObserver.assertNoErrors()
        responseTestObserver.assertSubscribed()
        responseTestObserver.assertComplete()
        assertThat(responseTestObserver.values()[0].location!!.name, `is`("Patna"))
        assertThat(responseTestObserver.values()[0].location!!.region, `is`("Bihar"))
        assertThat(responseTestObserver.values()[0].location!!.country, `is`("India"))

    }

    @Test
    fun handleTemperatureResponse() {
        presenter.handleTemperatureResponse(null)
        Mockito.verify(view).handleErrorView(true)

        Mockito.`when`(temperatureResponse!!.location).thenReturn(location)
        Mockito.`when`(temperatureResponse!!.current).thenReturn(current)
        presenter.handleTemperatureResponse(temperatureResponse)
        Mockito.verify(view).handleLoaderView(false)

        Mockito.`when`(temperatureResponse!!.error).thenReturn(error)
        Mockito.`when`<String>(error!!.message).thenReturn(errorText)
        presenter.handleTemperatureResponse(temperatureResponse)
        Mockito.verify(view).showErrorMessage(errorText)
        Mockito.verify(view).handleWeatherView(true)

    }
}