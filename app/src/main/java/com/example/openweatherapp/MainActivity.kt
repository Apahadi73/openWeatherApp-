package com.example.openweatherapp

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.openweatherapp.databinding.ActivityMainBinding
import org.json.JSONException
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mQueue: RequestQueue? = null
    private var cityName = "tyler"
    private val API = "4cba5dd445c51e36ffd7fdb3568ab036"
    private var url = "http://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=${API}&units=metric"
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_main)
        mQueue = Volley.newRequestQueue(this)
        weatherInfo

        binding.textfield.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            Log.d("response",cityName)
                            cityName= binding.textfield.text.toString()
                            weatherInfo
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        })
    }

    private val weatherInfo: Unit
        get() {
            val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    Log.d("response", response.toString())
                    val main = response.getJSONObject("main")
                    val sys = response.getJSONObject("sys")
                    val wind = response.getJSONObject("wind")
                    val weather = response.getJSONArray("weather").getJSONObject(0)
                    val date = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(
                            response.getLong(
                                "dt"
                            ) * 1000
                        )
                    )
                    val temp = main.getString("temp") + "°C"
                    val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                    val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                    val feelsLike = "Feels Like: " + main.getString("feels_like") + "°C"
                    val pressure = main.getString("pressure") + "hPa"
                    val humidity = main.getString("humidity")
                    val sunrise = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                        Date(
                            sys.getLong(
                                "sunrise"
                            ) * 1000
                        )
                    )
                    val sunset = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                        Date(
                            sys.getLong(
                                "sunset"
                            ) * 1000
                        )
                    )
                    val windSpeed = wind.getString("speed") + "m/s"
                    val weatherDescription = weather.getString("description")
                    val address = response.getString("name") + ", " + sys.getString("country")

//                    populating the data
                    binding.address.text = address
                    binding.date.text = date
                    binding.weatherDescription.text = weatherDescription
                    binding.temp.text = temp
                    binding.feelsLike.text = feelsLike
                    binding.tempMin.text = tempMin
                    binding.tempMax.text = tempMax
                    binding.sunrise.text = sunrise
                    binding.sunset.text = sunset
                    binding.wind.text = windSpeed
                    binding.pressure.text = pressure
                    binding.humidity.text = humidity

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> // TODO: Handle error
                Log.d("myResponse", error.toString())
            }
            mQueue!!.add(request)
        }


}


